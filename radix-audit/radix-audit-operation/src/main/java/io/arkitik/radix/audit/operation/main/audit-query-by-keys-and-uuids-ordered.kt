package io.arkitik.radix.audit.operation.main

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.audit.sdk.query.AuditQueryRequest
import io.arkitik.radix.audit.store.query.AuditRecordStoreQuery
import io.arkitik.radix.audit.store.query.OrderProperty
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.store.PageableData
import io.arkitik.radix.develop.store.query.PageData
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
internal class AuditQueryByKeysAndUuidsOrdered<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    private val auditRecordStoreQuery: AuditRecordStoreQuery<ID, I, A>,
) : Operation<AuditQueryRequest<ID>, PageData<A>> {
    override fun AuditQueryRequest<ID>.operate() =
        auditRecordStoreQuery.findAllByKeyNameInAndRecordUuidInOrderedBy(
            keys = keys,
            recordUuids = recordUuids,
            sorting = sorting.map {
                OrderProperty(it.propertyName, it.ascending)
            },
            pageableData = PageableData(page.page, page.size)
        )
}
