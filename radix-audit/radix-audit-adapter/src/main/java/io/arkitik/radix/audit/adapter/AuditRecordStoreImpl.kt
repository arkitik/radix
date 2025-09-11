package io.arkitik.radix.audit.adapter

import io.arkitik.radix.adapter.shared.StoreImpl
import io.arkitik.radix.audit.adapter.query.AuditRecordStoreQueryImpl
import io.arkitik.radix.audit.adapter.repository.AuditRecordRepository
import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.audit.store.AuditRecordStore
import io.arkitik.radix.audit.store.query.AuditRecordStoreQuery
import io.arkitik.radix.develop.identity.Identity
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
class AuditRecordStoreImpl<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>, E : A>(
    repository: AuditRecordRepository<ID, I, A, E>,
) : StoreImpl<String, A, E>(
    repository = repository
), AuditRecordStore<ID, I, A> {
    @Suppress("UNCHECKED_CAST")
    override fun A.map() = this as E

    override val storeQuery: AuditRecordStoreQuery<ID, I, A> = AuditRecordStoreQueryImpl(repository)
}
