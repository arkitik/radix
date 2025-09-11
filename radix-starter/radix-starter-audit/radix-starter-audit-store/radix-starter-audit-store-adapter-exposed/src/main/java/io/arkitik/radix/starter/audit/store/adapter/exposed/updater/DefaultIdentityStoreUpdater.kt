package io.arkitik.radix.starter.audit.store.adapter.exposed.updater

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.starter.audit.store.core.updater.AuditStoreIdentityUpdater
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
class DefaultIdentityStoreUpdater<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    private val auditStoreIdentityCreator: AuditStoreIdentityUpdater<ID, I, A>,
) : StoreIdentityUpdater<ID, I> {
    override fun update(): I {
        return auditStoreIdentityCreator.update().identity
    }
}
