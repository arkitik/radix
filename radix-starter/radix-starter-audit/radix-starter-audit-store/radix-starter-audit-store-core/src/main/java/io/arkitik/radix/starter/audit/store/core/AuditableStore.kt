package io.arkitik.radix.starter.audit.store.core

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.Store
import io.arkitik.radix.starter.audit.store.core.creator.AuditStoreIdentityCreator
import io.arkitik.radix.starter.audit.store.core.updater.AuditStoreIdentityUpdater
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface AuditableStore<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>> : Store<ID, I> {

    fun identityCreator(
        actorId: String,
        actorType: String,
    ): AuditStoreIdentityCreator<ID, I, A>

    fun I.identityUpdater(
        actorId: String,
        actorType: String,
    ): AuditStoreIdentityUpdater<ID, I, A>
}
