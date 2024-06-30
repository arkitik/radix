package io.arkitik.radix.adapter.exposed.query

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.query.PageData
import io.arkitik.radix.develop.store.query.StoreQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.count
import org.jetbrains.exposed.sql.selectAll
import java.io.Serializable

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 4:37 PM, 26/06/2024
 */
open class ExposedStoreQuery<ID, I : Identity<ID>, IT : RadixTable<ID>>(
    protected val identityTable: IT,
    protected val mapToIdentity: ResultRow.() -> I,
) : StoreQuery<ID, I> where ID : Serializable, ID : Comparable<ID> {
    override fun all(): List<I> =
        identityTable.selectAll()
            .toList()
            .map { it.mapToIdentity() }

    override fun all(page: Int, size: Int): PageData<I> {
        val users = identityTable.selectAll()
            .limit(size, page.toLong())
            .map { it.mapToIdentity() }
            .toList()
        val totalElements = identityTable.select(identityTable.uuid.count()).count()
        return PageData(
            content = users,
            numberOfElements = users.count(),
            totalElements = totalElements,
            totalPages = (totalElements / size).toInt(),
            currentPage = page,
            currentPageSize = size
        )
    }

    override fun allByUuids(uuids: List<ID>): Iterable<I> =
        identityTable.selectAll().where {
            identityTable.uuid inList uuids
        }.map { it.mapToIdentity() }

    override fun find(uuid: ID): I? =
        identityTable.selectAll().where {
            identityTable.uuid eq uuid
        }.firstOrNull()?.mapToIdentity()

    override fun exist(uuid: ID): Boolean =
        !identityTable.select(identityTable.uuid).where {
            identityTable.uuid eq uuid
        }.empty()
}
