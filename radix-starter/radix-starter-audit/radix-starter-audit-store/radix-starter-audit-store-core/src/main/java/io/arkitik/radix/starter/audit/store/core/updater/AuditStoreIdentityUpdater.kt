package io.arkitik.radix.starter.audit.store.core.updater

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.starter.audit.store.core.log.AuditLogs
import io.arkitik.radix.starter.audit.store.core.operator.AuditOperator
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface AuditStoreIdentityUpdater<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>> :
    AuditOperator<I>, StoreIdentityUpdater<ID, AuditLogs<ID, I, A>>
