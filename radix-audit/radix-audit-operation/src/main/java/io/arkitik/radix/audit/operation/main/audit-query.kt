package io.arkitik.radix.audit.operation.main

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.audit.sdk.query.AuditQueryRequest
import io.arkitik.radix.audit.sdk.query.PagedData
import io.arkitik.radix.audit.store.query.AuditRecordStoreQuery
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.ext.runOperation
import io.arkitik.radix.develop.store.query.PageData
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
private typealias Predicate<ID> = (AuditQueryRequest<ID>) -> Boolean
private typealias QueryOperation<ID, A> = Operation<AuditQueryRequest<ID>, PageData<A>>

internal class AuditQueryOperation<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    auditRecordStoreQuery: AuditRecordStoreQuery<ID, I, A>,
) : Operation<AuditQueryRequest<ID>, PagedData<A>> {
    private val auditQueryByKeysAndUuidsOrdered = AuditQueryByKeysAndUuidsOrdered(auditRecordStoreQuery)
    private val auditQueryByUuidsOrdered = AuditQueryByUuidsOrdered(auditRecordStoreQuery)
    private val auditQueryByKeysOrdered = AuditQueryByKeysOrdered(auditRecordStoreQuery)
    private val auditQueryOrdered = AuditQueryOrdered(auditRecordStoreQuery)

    private val operationMapper: Map<QueryOperation<ID, A>, Predicate<ID>> = buildMap {
        put(auditQueryOrdered) {
            it.keys.isEmpty() and it.recordUuids.isEmpty()
        }
        put(auditQueryByKeysOrdered) {
            it.keys.isNotEmpty() and it.recordUuids.isEmpty()
        }
        put(auditQueryByUuidsOrdered) {
            it.keys.isEmpty() and it.recordUuids.isNotEmpty()
        }
        put(auditQueryByKeysAndUuidsOrdered) {
            it.keys.isNotEmpty() and it.recordUuids.isNotEmpty()
        }
    }

    override fun AuditQueryRequest<ID>.operate(): PagedData<A> {
        val queryOperation = operationMapper.entries
            .firstOrNull {
                it.value(this)
            }?.key ?: auditQueryOrdered
        return queryOperation.runOperation(this)
            .let {
                PagedData(
                    content = it.content,
                    numberOfElements = it.numberOfElements,
                    totalElements = it.totalElements,
                    totalPages = it.numberOfElements,
                    currentPage = it.currentPage,
                    currentPageSize = it.currentPageSize
                )
            }
    }
}
