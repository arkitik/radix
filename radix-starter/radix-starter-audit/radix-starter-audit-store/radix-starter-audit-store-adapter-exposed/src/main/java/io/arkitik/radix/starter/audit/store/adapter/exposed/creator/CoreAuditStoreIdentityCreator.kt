package io.arkitik.radix.starter.audit.store.adapter.exposed.creator

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.starter.audit.store.core.creator.AuditStoreIdentityCreator
import io.arkitik.radix.starter.audit.store.core.log.AuditLogData
import io.arkitik.radix.starter.audit.store.core.log.AuditLogs
import io.arkitik.radix.starter.audit.store.core.operator.AuditOperator
import java.io.Serializable


/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
abstract class CoreAuditStoreIdentityCreator<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    private val actorId: String,
    private val actorType: String,
) : AuditStoreIdentityCreator<ID, I, A> {
    private val auditLogs: MutableList<AuditLogData<String>> = mutableListOf()
    override fun <T> addHistoryRecord(log: AuditLogData<T>, mapper: (T) -> String?): AuditOperator<I> {
        auditLogs.add(
            AuditLogData(
                log.keyName,
                log.oldValue?.let(mapper),
                log.newValue?.let(mapper),
            )
        )
        return this
    }

    final override fun create(): AuditLogs<ID, I, A> {
        val identity = createIdentity()
        return AuditLogs(
            auditLogs.map {
                identity.createAudit(it, actorId, actorType)
            },
            identity
        )
    }

    abstract fun I.createAudit(
        log: AuditLogData<String>,
        actorId: String,
        actorType: String,
    ): A

    abstract fun createIdentity(): I
}
