package io.arkitik.radix.audit.sdk

import io.arkitik.radix.audit.sdk.query.AuditQueryRequest
import io.arkitik.radix.audit.sdk.query.PagedData
import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.operation.Operation
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface RadixAuditSdk<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>> {
    val insertAudits: Operation<List<A>, Unit>
    val auditQuery: Operation<AuditQueryRequest<ID>, PagedData<A>>
}
