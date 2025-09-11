package io.arkitik.radix.audit.operation.main

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.audit.store.AuditRecordStore
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.operation.Operation
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
internal class InsertAuditsOperation<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>>(
    private val auditRecordStore: AuditRecordStore<ID, I, A>,
) : Operation<List<A>, Unit> {
    override fun List<A>.operate() {
        with(auditRecordStore) {
            this@operate.filter {
                it.oldValue != it.newValue
            }.insertIgnore()
        }
    }
}
