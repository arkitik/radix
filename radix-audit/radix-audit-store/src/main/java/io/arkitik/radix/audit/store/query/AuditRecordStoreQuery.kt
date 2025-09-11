package io.arkitik.radix.audit.store.query

import io.arkitik.radix.audit.domain.AuditRecordIdentity
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.PageableData
import io.arkitik.radix.develop.store.query.PageData
import io.arkitik.radix.develop.store.query.StoreQuery
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
interface AuditRecordStoreQuery<ID : Serializable, I : Identity<ID>, A : AuditRecordIdentity<ID, I>> :
    StoreQuery<String, A> {
    fun findAllOrderedBy(
        sorting: List<OrderProperty>,
        pageableData: PageableData,
    ): PageData<A>

    fun findAllByRecordUuidInOrderedBy(
        recordUuids: List<ID>,
        sorting: List<OrderProperty>,
        pageableData: PageableData,
    ): PageData<A>

    fun findAllByKeyNameInOrderedBy(
        keys: List<String>,
        sorting: List<OrderProperty>,
        pageableData: PageableData,
    ): PageData<A>

    fun findAllByKeyNameInAndRecordUuidInOrderedBy(
        keys: List<String>,
        recordUuids: List<ID>,
        sorting: List<OrderProperty>,
        pageableData: PageableData,
    ): PageData<A>
}

data class OrderProperty(
    val keyName: String,
    val ascending: Boolean,
)
