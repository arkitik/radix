package io.arkitik.radix.audit.store

import io.arkitik.radix.audit.store.query.AuditRecordStoreQuery
import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.Store
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface AuditRecordStore<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>> :
    Store<String, A> {
    override val storeQuery: AuditRecordStoreQuery<ID, I, A>

    override fun identityCreator() =
        throw IllegalStateException("Audit records can be created only by the auditable entities, see @{radix-starter-audit}")

    override fun A.identityUpdater() =
        throw IllegalStateException("Audit records are not allowed to get updated.")
}
