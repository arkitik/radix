package io.arkitik.radix.starter.audit.store.adapter

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import io.arkitik.radix.starter.audit.store.adapter.creator.CoreAuditStoreIdentityCreator
import io.arkitik.radix.starter.audit.store.adapter.creator.DefaultIdentityStoreCreator
import io.arkitik.radix.starter.audit.store.adapter.updater.CoreAuditStoreIdentityUpdater
import io.arkitik.radix.starter.audit.store.adapter.updater.DefaultIdentityStoreUpdater
import io.arkitik.radix.starter.audit.store.core.AuditableStore
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
abstract class AuditableStoreImpl<ID : Serializable, I : Identity<ID>, E : I, A : AuditRecordIdentity<ID, I>>(
    repository: RadixRepository<ID, E>,
) : StoreImpl<ID, I, E>(
    repository
), AuditableStore<ID, I, A> {

    companion object {
        private const val RADIX_ACTOR_ID = "RADIX_AUDIT"
        private const val RADIX_ACTOR = "AUDIT-TALE-ACTOR"
    }

    override fun identityCreator(): StoreIdentityCreator<ID, I> {
        return DefaultIdentityStoreCreator(identityCreator(RADIX_ACTOR_ID, RADIX_ACTOR))
    }

    override fun I.identityUpdater(): StoreIdentityUpdater<ID, I> {
        return DefaultIdentityStoreUpdater(identityUpdater(RADIX_ACTOR_ID, RADIX_ACTOR))
    }

    abstract override fun identityCreator(actorId: String, actorType: String): CoreAuditStoreIdentityCreator<ID, I, A>

    abstract override fun I.identityUpdater(actorId: String, actorType: String): CoreAuditStoreIdentityUpdater<ID, I, A>
}
