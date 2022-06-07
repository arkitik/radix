package io.arkitik.radix.develop.store.audit.creator

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.identity.audit.AuditRecordIdentity
import io.arkitik.radix.develop.identity.audit.Auditable
import io.arkitik.radix.develop.store.audit.operator.AuditOperator
import java.io.Serializable

/**
 * Created By [**Ibrahim Al-Tamimi **](https://www.linkedin.com/in/iloom/)
 * Created At **Saturday **28**, May 2022**
 */
abstract class StoreIdentityCreator<ID : Serializable, I> : AuditOperator<I>() where I : Identity<ID>, I : Auditable {
    abstract fun ID.uuid(): StoreIdentityCreator<ID, I>
    abstract fun create(): Pair<String, List<AuditRecordIdentity<I>>>
}