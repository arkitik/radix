package io.arkitik.radix.audit.operation

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.audit.operation.main.AuditQueryOperation
import io.arkitik.radix.audit.operation.main.InsertAuditsOperation
import io.arkitik.radix.audit.sdk.RadixAuditSdk
import io.arkitik.radix.audit.sdk.query.AuditQueryRequest
import io.arkitik.radix.audit.sdk.query.PagedData
import io.arkitik.radix.audit.store.AuditRecordStore
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.ext.operationBuilder
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
class RadixAuditSdkImpl<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    auditRecordStore: AuditRecordStore<ID, I, A>,
) : RadixAuditSdk<ID, I, A> {

    override val insertAudits: Operation<List<A>, Unit> =
        operationBuilder {
            mainOperation(
                InsertAuditsOperation(
                    auditRecordStore = auditRecordStore
                )
            )
        }
    override val auditQuery: Operation<AuditQueryRequest<ID>, PagedData<A>> =
        operationBuilder {
            mainOperation(
                AuditQueryOperation(
                    auditRecordStoreQuery = auditRecordStore.storeQuery
                )
            )
        }
}
