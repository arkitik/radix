package io.arkitik.radix.adapter.shared

import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.Store
import io.arkitik.radix.develop.store.query.StoreQuery
import java.io.Serializable

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
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


    override fun I.save(): I = repository.save(this.map())
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

    override fun I.saveIgnore() {
        save()
    }

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
