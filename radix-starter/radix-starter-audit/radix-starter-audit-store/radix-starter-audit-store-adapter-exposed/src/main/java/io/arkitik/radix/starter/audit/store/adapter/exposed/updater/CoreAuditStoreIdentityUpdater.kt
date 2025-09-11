package io.arkitik.radix.starter.audit.store.adapter.exposed.updater

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.starter.audit.store.core.log.AuditLogData
import io.arkitik.radix.starter.audit.store.core.log.AuditLogs
import io.arkitik.radix.starter.audit.store.core.operator.AuditOperator
import io.arkitik.radix.starter.audit.store.core.updater.AuditStoreIdentityUpdater
import java.io.Serializable


/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
abstract class CoreAuditStoreIdentityUpdater<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    private val actorId: String,
    private val actorType: String,
) : AuditStoreIdentityUpdater<ID, I, A> {
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

    final override fun update(): AuditLogs<ID, I, A> {
        val identity = updateIdentity()
        return AuditLogs(
            auditLogs.map { auditLogData ->
                identity.createAudit(
                    log = auditLogData,
                    actorId = actorId,
                    actorType = actorType
                )
            },
            identity
        )
    }

    abstract fun I.createAudit(
        log: AuditLogData<String>,
        actorId: String,
        actorType: String,
    ): A

    abstract fun updateIdentity(): I
}
