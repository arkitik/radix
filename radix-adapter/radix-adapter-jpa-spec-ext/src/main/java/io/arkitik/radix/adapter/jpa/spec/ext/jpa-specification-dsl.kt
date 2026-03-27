package io.arkitik.radix.adapter.jpa.spec.ext

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.From
import jakarta.persistence.criteria.Join
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import kotlin.reflect.KProperty1

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 1:10 AM, 09 , **Thu, June 2022**
 * Project *radix* [arkitik.io](https://arkitik.io)
 * Forked from [kotlin-jpa-specification-dsl](https://github.com/consoleau/kotlin-jpa-specification-dsl/blob/master/src/main/kotlin/au/com/console/jpaspecificationdsl/JPASpecificationDSL.kt)
 */
// Helper to allow joining to Properties
fun <Z, T, R> From<Z, T>.join(prop: KProperty1<T, R?>): Join<T, R> = this.join<T, R>(prop.name)

// Helper to enable get by Property
fun <R> Path<*>.get(prop: KProperty1<*, R?>): Path<R> = this.get<R>(prop.name)

// Version of Specification.where that makes the CriteriaBuilder implicit
fun <T : Any> where(makePredicate: CriteriaBuilder.(Root<T>) -> Predicate): Specification<T> =
    Specification { root, _, criteriaBuilder -> criteriaBuilder.makePredicate(root) }

// helper function for defining Specification that take a Path to a property and send it to a CriteriaBuilder
private fun <T : Any, R> KProperty1<T, R?>.spec(makePredicate: CriteriaBuilder.(path: Path<R>) -> Predicate): Specification<T> =
    where { root -> makePredicate(root.get(this@spec)) }

// Equality
fun <T : Any, R> KProperty1<T, R?>.equal(x: R): Specification<T> = spec { equal(it, x) }

fun <T : Any, R> KProperty1<T, R?>.notEqual(x: R): Specification<T> = spec { notEqual(it, x) }

// Ignores empty collection otherwise an empty 'in' predicate will be generated which will never match any results
fun <T : Any, R : Any> KProperty1<T, R?>.`in`(values: Collection<R>): Specification<T> =
    if (values.isNotEmpty()) spec { path ->
        `in`(path).apply { values.forEach { this.value(it) } }
    } else Specification { _, _, _ -> null }

// Comparison
fun <T : Any> KProperty1<T, Number?>.le(x: Number) = spec { le(it, x) }

fun <T : Any> KProperty1<T, Number?>.lt(x: Number) = spec { lt(it, x) }
fun <T : Any> KProperty1<T, Number?>.ge(x: Number) = spec { ge(it, x) }
fun <T : Any> KProperty1<T, Number?>.gt(x: Number) = spec { gt(it, x) }
fun <T : Any, R : Comparable<R>> KProperty1<T, R?>.lessThan(x: R) = spec { lessThan(it, x) }
fun <T : Any, R : Comparable<R>> KProperty1<T, R?>.lessThanOrEqualTo(x: R) = spec { lessThanOrEqualTo(it, x) }
fun <T : Any, R : Comparable<R>> KProperty1<T, R?>.greaterThan(x: R) = spec { greaterThan(it, x) }
fun <T : Any, R : Comparable<R>> KProperty1<T, R?>.greaterThanOrEqualTo(x: R) = spec { greaterThanOrEqualTo(it, x) }
fun <T : Any, R : Comparable<R>> KProperty1<T, R?>.between(x: R, y: R) = spec { between(it, x, y) }

// True/False
fun <T : Any> KProperty1<T, Boolean?>.isTrue() = spec { isTrue(it) }

fun <T : Any> KProperty1<T, Boolean?>.isFalse() = spec { isFalse(it) }

// Null / NotNull
fun <T : Any, R> KProperty1<T, R?>.isNull() = spec { isNull(it) }

fun <T : Any, R> KProperty1<T, R?>.isNotNull() = spec { isNotNull(it) }

// Collections
fun <T : Any, R : Collection<*>> KProperty1<T, R?>.isEmpty() = spec { isEmpty(it) }

fun <T : Any, R : Collection<*>> KProperty1<T, R?>.isNotEmpty() = spec { isNotEmpty(it) }
fun <T : Any, E, R : Collection<E>> KProperty1<T, R?>.isMember(elem: E) = spec { isMember(elem, it) }
fun <T : Any, E, R : Collection<E>> KProperty1<T, R?>.isNotMember(elem: E) = spec { isNotMember(elem, it) }

// Strings
fun <T : Any> KProperty1<T, String?>.like(x: String): Specification<T> = spec { like(it, x) }

fun <T : Any> KProperty1<T, String?>.like(x: String, escapeChar: Char): Specification<T> =
    spec { like(it, x, escapeChar) }

fun <T : Any> KProperty1<T, String?>.notLike(x: String): Specification<T> = spec { notLike(it, x) }
fun <T : Any> KProperty1<T, String?>.notLike(x: String, escapeChar: Char): Specification<T> =
    spec { notLike(it, x, escapeChar) }

// And
infix fun <T : Any> Specification<T>.and(other: Specification<T>): Specification<T> = this.and(other)

inline fun <reified T : Any> and(vararg specs: Specification<T>?): Specification<T> =
    and(specs.toList())

inline fun <reified T : Any> and(specs: Iterable<Specification<T>?>): Specification<T> =
    combineSpecification(specs, Specification<T>::and)

// Or
infix fun <T : Any> Specification<T>.or(other: Specification<T>): Specification<T> = this.or(other)

inline fun <reified T : Any> or(vararg specs: Specification<T>?): Specification<T> =
    or(specs.toList())

inline fun <reified T : Any> or(specs: Iterable<Specification<T>?>): Specification<T> =
    combineSpecification(specs, Specification<T>::or)

// Not
operator fun <T : Any> Specification<T>.not(): Specification<T> = Specification.not(this)

// Combines Specification with an operation
inline fun <reified T : Any> combineSpecification(
    specs: Iterable<Specification<T>?>,
    operation: Specification<T>.(Specification<T>) -> Specification<T>,
): Specification<T> = specs.filterNotNull().reduceOrNull { existing, new -> existing.operation(new) }
    ?: emptySpecification()

// Empty Specification
fun <T : Any> emptySpecification(): Specification<T> = Specification { _, _, _ -> null }
