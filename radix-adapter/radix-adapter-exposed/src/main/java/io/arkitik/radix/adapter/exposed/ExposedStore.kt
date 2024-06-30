package io.arkitik.radix.adapter.exposed

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.Store
import io.arkitik.radix.develop.store.query.StoreQuery
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert
import java.io.Serializable

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:36 PM, 26/06/2024
 */
abstract class ExposedStore<ID, I : Identity<ID>, IT : RadixTable<ID>>(
    protected val identityTable: IT,
) : Store<ID, I> where ID : Serializable, ID : Comparable<ID> {
    override val storeQuery: StoreQuery<ID, I>
        get() = ExposedStoreQuery(identityTable) { this.mapToIdentity() }

    override fun I.delete() {
        uuid?.let { uuid ->
            identityTable.deleteWhere { identityTable.uuid eq uuid }
        }
    }

    override fun ID.delete() {
        identityTable.deleteWhere { identityTable.uuid eq this@delete }
    }

    override fun List<I>.deleteAll() {
        identityTable.deleteWhere {
            identityTable.uuid inList mapNotNull { it.uuid }
        }
    }

    override fun List<ID>.deleteAllByIds() {
        identityTable.deleteWhere { identityTable.uuid inList this@deleteAllByIds }
    }

    override fun I.save() =
        transaction {
            this@save.also {
                identityTable.upsert {
                    it.createDefaultEntity(this@save)
                }
            }
        }

    override fun List<I>.save() =
        transaction {
            identityTable.batchUpsert(this@save) {
                this.createDefaultEntity(it)
            }.map { it.mapToIdentity() }
        }

    private fun <K : Any> InsertStatement<K>.createDefaultEntity(it: I) {
        it.uuid?.also {
            this[identityTable.uuid] = it
        }
        this[identityTable.creationDate] = it.creationDate
        this.createEntity(it)
    }

    protected abstract fun <K : Any> InsertStatement<K>.createEntity(it: I)

    protected abstract fun ResultRow.mapToIdentity(): I
}
