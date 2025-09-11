package io.arkitik.radix.starter.audit.store.core.log

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
open class AuditLogs<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    open val logs: List<A>,
    val identity: I,
) : Identity<ID> {
    override val uuid = identity.uuid
    override val creationDate = identity.creationDate
}

data class AuditLogData<T>(
    val keyName: String,
    val oldValue: T?,
    val newValue: T?,
)
