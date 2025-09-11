package io.arkitik.radix.audit.domain

import io.arkitik.radix.audit.domain.embedded.ActorType
import io.arkitik.radix.audit.domain.embedded.AuditChangeType
import io.arkitik.radix.develop.identity.Identity
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface AuditRecordIdentity<ID : Serializable, I : Identity<ID>> : Identity<String> {
    override val uuid: String
    val record: I
    val keyName: String
    val oldValue: String?
    val newValue: String?
    val actorId: String
    val actorType: ActorType
    val changeType: AuditChangeType
}
