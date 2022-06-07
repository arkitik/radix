package io.arkitik.radix.develop.identity.audit

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.identity.audit.embedded.AuditChangeType
import io.arkitik.radix.develop.identity.audit.embedded.ActorType
import java.io.Serializable

/**
 * Created By [**Ibrahim Al-Tamimi **](https://www.linkedin.com/in/iloom/)
 * Created At **Saturday **28**, May 2022**
 */
interface AuditRecordIdentity<I> : Identity<String> where I : Identity<out Serializable>, I : Auditable {
    override val uuid: String
    val record: I
    val keyName: String
    val oldValue: String?
    val newValue: String?
    val actorId: String
    val actorType: ActorType
    val changeType: AuditChangeType
}