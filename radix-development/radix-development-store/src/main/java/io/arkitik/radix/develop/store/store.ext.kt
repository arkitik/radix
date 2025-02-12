package io.arkitik.radix.develop.store

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import java.io.Serializable

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
fun <ID : Serializable, I : Identity<ID>, SIU : StoreIdentityUpdater<ID, I>> storeUpdater(
    storeIdentityUpdater: SIU,
    updaterFun: SIU.() -> I,
): I = storeIdentityUpdater.updaterFun()

@Deprecated(
    message = "This method will be removed in a future version. Use 'storeUpdaterWithUpdate()' for modifying existing records",
    replaceWith = ReplaceWith(
        expression = "storeUpdaterWithUpdate()",
    ),
    level = DeprecationLevel.WARNING
)
fun <ID : Serializable, I : Identity<ID>, SIU : StoreIdentityUpdater<ID, I>> Store<ID, I>.storeUpdaterWithSave(
    storeIdentityUpdater: SIU,
    updaterFun: SIU.() -> I,
): I = storeUpdater(storeIdentityUpdater, updaterFun).save()

fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> storeCreator(
    creator: SIC,
    creatorFun: SIC.() -> I,
): I = creator.run(creatorFun)

@Deprecated(
    message = "This method will be removed in a future version. Use 'storeCreatorWithInsert()' for creating new records",
    replaceWith = ReplaceWith(
        expression = "storeCreatorWithInsert()",
    ),
    level = DeprecationLevel.WARNING
)
fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> Store<ID, I>.storeCreatorWithSave(
    creator: SIC,
    creatorFun: SIC.() -> I,
): I = storeCreator(creator, creatorFun).save()


fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> creator(
    creator: SIC,
    creatorCustomizer: SIC.() -> Unit,
): I = creator.apply(creatorCustomizer).create()

@Deprecated(
    message = "This method will be removed in a future version. Use 'creatorWithInsert()' for creating new records",
    replaceWith = ReplaceWith(
        expression = "creatorWithInsert()",
    ),
    level = DeprecationLevel.WARNING
)
fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> Store<ID, I>.creatorWithSave(
    creator: SIC,
    customizer: SIC.() -> Unit,
): I = creator(creator, customizer).save()


fun <ID : Serializable, I : Identity<ID>, SIU : StoreIdentityUpdater<ID, I>> Store<ID, I>.storeUpdaterWithUpdate(
    storeIdentityUpdater: SIU,
    updaterFun: SIU.() -> I,
): I = storeUpdater(storeIdentityUpdater, updaterFun).update()

fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> Store<ID, I>.storeCreatorWithInsert(
    creator: SIC,
    creatorFun: SIC.() -> I,
): I = storeCreator(creator, creatorFun).insert()

fun <ID : Serializable, I : Identity<ID>, SIC : StoreIdentityCreator<ID, I>> Store<ID, I>.creatorWithInsert(
    creator: SIC,
    customizer: SIC.() -> Unit,
): I = creator(creator, customizer).insert()

