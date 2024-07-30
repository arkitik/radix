package io.arkitik.radix.develop.store

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import java.io.Serializable

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
typealias TransactionCommand<T> = () -> T

interface Store<ID : Serializable, I : Identity<ID>> {
    fun ID.delete()
    fun I.delete()
    fun List<ID>.deleteAllByIds()
    fun List<I>.deleteAll()
    val storeQuery: StoreQuery<ID, I>
    fun identityCreator(): StoreIdentityCreator<ID, I>
    fun I.identityUpdater(): StoreIdentityUpdater<ID, I>

    fun I.save(): I
    fun List<I>.save(): Iterable<I>

    fun I.insert(): I
    fun List<I>.insert(): Iterable<I>

    fun I.update(): I
    fun List<I>.update(): Iterable<I>

    fun I.saveIgnore()
    fun List<I>.saveIgnore()

    fun I.insertIgnore()
    fun List<I>.insertIgnore()

    fun I.updateIgnore()
    fun List<I>.updateIgnore()

    fun <T> executeInTransaction(transactionCommand: TransactionCommand<T>): T =
        transactionCommand()
}
