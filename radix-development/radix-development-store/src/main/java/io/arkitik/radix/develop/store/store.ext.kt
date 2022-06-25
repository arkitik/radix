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

fun <ID : Serializable, I : Identity<ID>, SIU : StoreIdentityUpdater<ID, I>> storeUpdater(
    storeIdentityUpdater: SIU,
    updaterFun: SIU.() -> I,
): I = storeIdentityUpdater.updaterFun()


fun <ID : Serializable, I : Identity<ID>, SIU : StoreIdentityUpdater<ID, I>> Store<ID, I>.storeUpdaterWithSave(
    storeIdentityUpdater: SIU,
    updaterFun: SIU.() -> I,
): I = storeUpdater(storeIdentityUpdater, updaterFun).save()

fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> storeCreator(
    creator: SIC,
    creatorFun: SIC.() -> I,
): I = creator.run(creatorFun)

fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> Store<ID, I>.storeCreatorWithSave(
    creator: SIC,
    creatorFun: SIC.() -> I,
): I = storeCreator(creator, creatorFun).save()


fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> creator(
    creator: SIC,
    creatorCustomizer: SIC.() -> Unit,
): I = creator.apply(creatorCustomizer).create()

fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> Store<ID, I>.creatorWithSave(
    creator: SIC,
    customizer: SIC.() -> Unit,
): I = creator(creator, customizer).save()
