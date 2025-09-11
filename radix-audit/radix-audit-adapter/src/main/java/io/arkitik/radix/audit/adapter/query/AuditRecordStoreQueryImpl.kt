package io.arkitik.radix.audit.adapter.query

import io.arkitik.radix.adapter.shared.paged
import io.arkitik.radix.adapter.shared.query.StoreQueryImpl
import io.arkitik.radix.audit.adapter.repository.AuditRecordRepository
import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.audit.store.query.AuditRecordStoreQuery
import io.arkitik.radix.audit.store.query.OrderProperty
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.PageableData
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
open class AuditRecordStoreQueryImpl<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>, E : A>(
    private val repository: AuditRecordRepository<ID, I, A, E>,
) : StoreQueryImpl<String, A, E>(
    repository
), AuditRecordStoreQuery<ID, I, A> {
    companion object {
        fun List<OrderProperty>.asSortBy() =
            Sort.by(
                map {
                    if (it.ascending)
                        Sort.Order.asc(
                            it.keyName
                        )
                    else
                        Sort.Order.desc(
                            it.keyName
                        )
                }
            )

    }

    override fun findAllOrderedBy(
        sorting: List<OrderProperty>,
        pageableData: PageableData,
    ) = paged(
        repository.findAll(
            PageRequest.of(
                pageableData.page,
                pageableData.size,
                sorting.asSortBy()
            )
        )
    )

    override fun findAllByRecordUuidInOrderedBy(
        recordUuids: List<ID>,
        sorting: List<OrderProperty>,
        pageableData: PageableData,
    ) = paged(
        repository.findAllByRecordUuidIn(
            uuids = recordUuids,
            pageable = PageRequest.of(
                pageableData.page,
                pageableData.size,
                sorting.asSortBy()
            )
        )
    )

    override fun findAllByKeyNameInOrderedBy(
        keys: List<String>,
        sorting: List<OrderProperty>,
        pageableData: PageableData,
    ) = paged(
        repository.findAllByKeyNameIn(
            keyNames = keys,
            pageable = PageRequest.of(
                pageableData.page,
                pageableData.size,
                sorting.asSortBy()
            )
        )
    )

    override fun findAllByKeyNameInAndRecordUuidInOrderedBy(
        keys: List<String>,
        recordUuids: List<ID>,
        sorting: List<OrderProperty>,
        pageableData: PageableData,
    ) = paged(
        repository.findAllByKeyNameInAndRecordUuidIn(
            keyNames = keys,
            uuids = recordUuids,
            pageable = PageRequest.of(
                pageableData.page,
                pageableData.size,
                sorting.asSortBy()
            )
        )
    )
}