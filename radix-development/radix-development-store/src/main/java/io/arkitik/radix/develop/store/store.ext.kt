package io.arkitik.radix.develop.store

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import java.io.Serializable

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.save(identity: I) = identity.save()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.save(identities: List<I>) = identities.save()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.delete(identity: I) = identity.delete()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.deleteId(id: ID) = id.delete()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.delete(identities: List<I>) = identities.deleteAll()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.deleteIds(ids: List<ID>) = ids.deleteAllByIds()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.updater(
    identity: I,
): StoreIdentityUpdater<ID, I> = identity.identityUpdater()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.creator(
    creatorFun: StoreIdentityCreator<ID, I>.() -> StoreIdentityCreator<ID, I>,
): StoreIdentityCreator<ID, I> = creatorFun(identityCreator())

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.creatorDo(
    creatorFun: StoreIdentityCreator<ID, I>.() -> I,
): I = creatorFun(identityCreator())

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.saveCreator(
    creatorFun: StoreIdentityCreator<ID, I>.() -> I,
): I = this save creatorDo(creatorFun)

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.saveCreatorFun(
    creatorFun: StoreIdentityCreator<ID, I>.() -> StoreIdentityCreator<ID, I>,
): I = creatorFun(identityCreator()).create().save()
