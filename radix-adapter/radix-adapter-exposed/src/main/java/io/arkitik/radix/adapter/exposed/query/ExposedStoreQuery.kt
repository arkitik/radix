package io.arkitik.radix.adapter.exposed.query

import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.query.PageData
import io.arkitik.radix.develop.store.query.StoreQuery
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.Serializable

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 4:37 PM, 26/06/2024
 */
open class ExposedStoreQuery<ID, I : Identity<ID>, IT : RadixTable<ID, I>>(
    protected val identityTable: IT,
) : StoreQuery<ID, I> where ID : Serializable, ID : Comparable<ID> {

    protected open fun Query.paged(page: Int, size: Int): PageData<I> =
        transaction {
            val totalElements = count()
            val items = limit(size, (size * page).toLong())
                .map(identityTable::mapToIdentity)
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
        transaction {
            identityTable.selectAll().map(identityTable::mapToIdentity)
        }

    override fun all(page: Int, size: Int): PageData<I> {
        return transaction {
            identityTable.selectAll().paged(page, size)
        }
    }

    override fun allByUuids(uuids: List<ID>): Iterable<I> =
        transaction {
            identityTable.selectAll().where {
                identityTable.uuid inList uuids
            }.map(identityTable::mapToIdentity)
        }

    override fun find(uuid: ID): I? =
        transaction { identityTable.findIdentityByUuid(uuid) }

    override fun exist(uuid: ID): Boolean =
        transaction {
            !identityTable.select(identityTable.uuid).where {
                identityTable.uuid eq uuid
            }.empty()
        }
}
