package io.arkitik.radix.starter.audit.store.adapter.exposed.creator

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.starter.audit.store.core.creator.AuditStoreIdentityCreator
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
class DefaultIdentityStoreCreator<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    private val auditStoreIdentityCreator: AuditStoreIdentityCreator<ID, I, A>,
) : StoreIdentityCreator<ID, I> {
    override fun ID.uuid(): StoreIdentityCreator<ID, I> {
        auditStoreIdentityCreator.run {
            this@uuid.uuid()
        }
        return this@DefaultIdentityStoreCreator
    }

    override fun create(): I {
        return auditStoreIdentityCreator.create().identity
    }
}
