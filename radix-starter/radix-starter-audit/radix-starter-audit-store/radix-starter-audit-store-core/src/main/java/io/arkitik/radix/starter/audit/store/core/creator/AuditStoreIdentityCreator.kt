package io.arkitik.radix.starter.audit.store.core.creator

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.radix.starter.audit.store.core.log.AuditLogs
import io.arkitik.radix.starter.audit.store.core.operator.AuditOperator
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface AuditStoreIdentityCreator<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>> :
    AuditOperator<I>, StoreIdentityCreator<ID, AuditLogs<ID, I, A>>
