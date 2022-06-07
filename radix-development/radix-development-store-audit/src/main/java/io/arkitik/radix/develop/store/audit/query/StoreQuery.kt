package io.arkitik.radix.develop.store.audit.query

import io.arkitik.radix.develop.identity.Identity
import java.io.Serializable

/**
 * Created By [**Ibrahim Al-Tamimi **](https://www.linkedin.com/in/iloom/)
 * Created At **Saturday **28**, May 2022**
 */
interface StoreQuery<ID : Serializable, I : Identity<ID>> {
    fun find(uuid: ID): I?
    fun exist(uuid: ID): Boolean
    fun all(): List<I>
    fun all(page: Int, size: Int): PageData<I>
    fun allByUuids(uuids: List<ID>): Iterable<I>
}

data class PageData<T>(
    val content: Iterable<T>,
    val numberOfElements: Int,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val currentPageSize: Int,
)