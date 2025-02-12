package io.arkitik.radix.adapter.shared

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.Store
import io.arkitik.radix.develop.store.query.StoreQuery
import java.io.Serializable

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
abstract class StoreImpl<ID : Serializable, I : Identity<ID>, E : I>(
    private val repository: RadixRepository<ID, E>,
) : Store<ID, I> {
    abstract fun I.map(): E

    override val storeQuery: StoreQuery<ID, I> = StoreQueryImpl(repository)

    override fun ID.delete() = repository.deleteById(this)
    override fun I.delete() = repository.delete(map())


    @Deprecated(
        "This method will be removed in a future version. Use 'update()' for modifying existing records or 'insert()' for creating new ones.",
        replaceWith = ReplaceWith("insert() or update()"),
        level = DeprecationLevel.WARNING
    )
    override fun I.save(): I = repository.save(this.map())

    @Deprecated(
        "This method will be removed in a future version. Use 'update()' for modifying existing records or 'insert()' for creating new ones.",
        replaceWith = ReplaceWith("insert() or update()"),
        level = DeprecationLevel.WARNING
    )
    override fun List<I>.save(): Iterable<I> = repository.saveAll(map {
        it.map()
    })

    override fun I.insert(): I = save()

    override fun List<I>.insert(): Iterable<I> = save()

    override fun I.update(): I = save()

    override fun List<I>.update(): Iterable<I> = save()

    override fun List<I>.deleteAll() =
        with(repository) {
            deleteAll(map {
                it.map()
            })
        }

    override fun List<ID>.deleteAllByIds() =
        forEach {
            repository.deleteById(it)
        }

    @Deprecated(
        "This method will be removed in a future version. Use 'updateIgnore()' for modifying existing records or 'insertIgnore()' for creating new ones.",
        replaceWith = ReplaceWith("insertIgnore() or updateIgnore()"),
        level = DeprecationLevel.WARNING
    )
    override fun I.saveIgnore() {
        save()
    }

    @Deprecated(
        "This method will be removed in a future version. Use 'updateIgnore()' for modifying existing records or 'insertIgnore()' for creating new ones.",
        replaceWith = ReplaceWith("insertIgnore() or updateIgnore()"),
        level = DeprecationLevel.WARNING
    )
    override fun List<I>.saveIgnore() {
        save()
    }

    override fun I.insertIgnore() {
        insert()
    }

    override fun List<I>.insertIgnore() {
        insert()
    }

    override fun I.updateIgnore() {
        update()
    }

    override fun List<I>.updateIgnore() {
        update()
    }
}
