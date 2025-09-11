package io.arkitik.radix.audit.adapter.repository

import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
@NoRepositoryBean
interface AuditRecordRepository<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>, E : A> :
    RadixRepository<String, E> {
    fun findAllByRecordUuidIn(uuids: List<ID>, pageable: Pageable): Page<E>
    fun findAllByKeyNameIn(keyNames: List<String>, pageable: Pageable): Page<E>

    fun findAllByKeyNameInAndRecordUuidIn(
        keyNames: List<String>,
        uuids: List<ID>,
        pageable: Pageable,
    ): Page<E>
}
