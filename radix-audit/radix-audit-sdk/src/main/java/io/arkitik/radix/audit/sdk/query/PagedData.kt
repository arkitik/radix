package io.arkitik.radix.audit.sdk.query

/**
 *
 * @author Ibrahim Al-Tamimi 
 * @since 08:53, Thursday, 11/09/2025
 **/
data class PagedData<T>(
    val content: Iterable<T>,
    val numberOfElements: Int,
    val totalElements: Long,
    val totalPages: Int,
    val currentPage: Int,
    val currentPageSize: Int,
)
