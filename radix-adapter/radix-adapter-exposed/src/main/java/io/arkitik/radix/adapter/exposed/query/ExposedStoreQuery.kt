package io.arkitik.radix.adapter.exposed.query

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.query.PageData
import io.arkitik.radix.develop.store.query.StoreQuery
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.Serializable

typealias ResultRowMapper<T> = (resultRow: ResultRow) -> T

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 4:37 PM, 26/06/2024
 */
open class ExposedStoreQuery<ID, I : Identity<ID>, IT : RadixTable<ID, I>>(
    protected val identityTable: IT,
    protected val database: Database? = null,
) : StoreQuery<ID, I> where ID : Serializable, ID : Comparable<ID> {
    protected open fun Query.paged(
        page: Int,
        size: Int,
    ): PageData<I> = paged(page, size) { rowItem ->
        identityTable.mapToIdentity(rowItem, database)
    }

    protected open fun <T> Query.paged(
        page: Int,
        size: Int,
        resultRowMapper: ResultRowMapper<T>,
    ): PageData<T> =
        transaction(database) {
            val totalElements = count()
            val items = limit(size).offset((size * page).toLong())
                .map(resultRowMapper)
            var totalPages = if (totalElements != 0L)
                totalElements / size
            else 0
            if ((totalElements % size).toInt() != 0) {
                totalPages += 1
            }
            PageData(
                content = items,
                numberOfElements = items.count(),
                totalElements = totalElements,
                totalPages = totalPages.toInt(),
                currentPage = page,
                currentPageSize = size
            )
        }

    override fun all(): List<I> =
        transaction(database) {
            identityTable.select(identityTable.columns)
                .map { rowItem ->
                    identityTable.mapToIdentity(rowItem, database)
                }
        }

    override fun all(page: Int, size: Int): PageData<I> {
        return transaction(database) {
            identityTable.select(identityTable.columns)
                .paged(page, size)
        }
    }

    override fun allByUuids(uuids: List<ID>): Iterable<I> =
        transaction(database) {
            identityTable.select(identityTable.columns)
                .where {
                    identityTable.uuid inList uuids
                }.map { rowItem ->
                    identityTable.mapToIdentity(rowItem, database)
                }
        }

    override fun find(uuid: ID): I? =
        transaction(database) {
            identityTable.findIdentityByUuid(uuid, database)
        }

    override fun exist(uuid: ID): Boolean =
        transaction(database) {
            identityTable.select(identityTable.uuid).where {
                identityTable.uuid eq uuid
            }.exist()
        }

    protected fun Query.exist() = empty().not()

    protected fun Query.doesNotExist() = empty()
}
