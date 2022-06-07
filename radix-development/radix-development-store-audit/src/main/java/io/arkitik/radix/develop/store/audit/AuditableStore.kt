package io.arkitik.radix.develop.store.audit

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.identity.audit.Auditable
import io.arkitik.radix.develop.identity.audit.embedded.ActorType
import io.arkitik.radix.develop.store.audit.creator.StoreIdentityCreator
import io.arkitik.radix.develop.store.audit.query.StoreQuery
import io.arkitik.radix.develop.store.audit.updater.StoreIdentityUpdater
import java.io.Serializable

/**
 * Created By [**Ibrahim Al-Tamimi **](https://www.linkedin.com/in/iloom/)
 * Created At **Saturday **28**, May 2022**
 */
interface AuditableStore<ID : Serializable, I> where I : Identity<ID>, I : Auditable {
    fun I.save(): I
    fun List<I>.save(): Iterable<I>
    fun ID.delete()
    fun I.delete()
    fun List<ID>.deleteAllByIds()
    fun List<I>.deleteAll()
    val storeQuery: StoreQuery<ID, I>
    fun identityCreator(
        actorId: String,
        actorType: ActorType,
    ): StoreIdentityCreator<ID, I>

    fun I.identityUpdater(
        actorId: String,
        actorType: ActorType,
    ): StoreIdentityUpdater<ID, I>
}