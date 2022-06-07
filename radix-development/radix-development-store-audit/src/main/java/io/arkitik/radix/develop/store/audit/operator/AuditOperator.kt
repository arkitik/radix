package io.arkitik.radix.develop.store.audit.operator

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.identity.audit.Auditable
import java.io.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

/**
 * Created By [**Ibrahim Al-Tamimi **](https://www.linkedin.com/in/iloom/)
 * Created At **Saturday **28**, May 2022**
 */
abstract class AuditOperator<A : Auditable> {
    protected abstract fun addHistoryRecord(
        keyName: String,
        oldValue: String?,
        newValue: String?,
    ): AuditOperator<A>

    protected fun <T> addHistoryRecord(
        keyName: String,
        oldValue: T?,
        newValue: T?,
    ) = addHistoryRecord(
        keyName = keyName,
        oldValue = oldValue,
        newValue = newValue,
        generator = Objects::toString
    )

    protected fun <T : Enum<*>> addHistoryRecord(
        keyName: String,
        oldValue: T?,
        newValue: T?,
    ) = addHistoryRecord(
        keyName = keyName,
        oldValue = oldValue,
        newValue = newValue,
    ) { it.name }

    protected fun <T : Identity<out Serializable>> addHistoryRecord(
        keyName: String,
        oldValue: T?,
        newValue: T?,
    ) = addHistoryRecord(
        keyName = keyName,
        oldValue = oldValue,
        newValue = newValue,
    ) {
        it.uuid.toString()
    }

    protected fun addHistoryRecord(
        keyName: String,
        oldValue: BigDecimal?,
        newValue: BigDecimal?,
    ) = addHistoryRecord(
        keyName = keyName,
        oldValue = oldValue,
        newValue = newValue,
        generator = BigDecimal::toEngineeringString
    )

    protected fun addHistoryRecord(
        keyName: String,
        oldValue: LocalDate?,
        newValue: LocalDate?,
    ) = addHistoryRecord(
        keyName = keyName,
        oldValue = oldValue,
        newValue = newValue,
        generator = LocalDate::toString
    )

    protected fun addHistoryRecord(
        keyName: String,
        oldValue: LocalDateTime?,
        newValue: LocalDateTime?,
    ) = addHistoryRecord(
        keyName = keyName,
        oldValue = oldValue,
        newValue = newValue,
        generator = LocalDateTime::toString
    )

    protected fun <T> addHistoryRecord(
        keyName: String,
        oldValue: T?,
        newValue: T?,
        generator: (T) -> String,
    ) = addHistoryRecord(
        keyName = keyName,
        oldValue = oldValue?.let { generator(it) },
        newValue = newValue?.let { generator(it) }
    )
}