package io.arkitik.radix.adapter.exposed

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.Store
import io.arkitik.radix.develop.store.TransactionCommand
import io.arkitik.radix.develop.store.query.StoreQuery
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.batchReplace
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertReturning
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.updateReturning
import org.jetbrains.exposed.sql.upsertReturning
import java.io.Serializable

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:36 PM, 26/06/2024
 */
abstract class ExposedStore<ID, I : Identity<ID>, IT : RadixTable<ID, I>>(
    protected val identityTable: IT,
) : Store<ID, I> where ID : Serializable, ID : Comparable<ID> {

    protected abstract fun <K : Any> UpdateBuilder<K>.createEntity(it: I)

    override val storeQuery: StoreQuery<ID, I>
        get() = ExposedStoreQuery(identityTable)

    override fun I.delete() {
        transaction {
            uuid?.let { uuid ->
                identityTable.deleteWhere { identityTable.uuid eq uuid }
            }
        }
    }

    override fun ID.delete() {
        transaction {
            identityTable.deleteWhere { identityTable.uuid eq this@delete }
        }
    }

    override fun List<I>.deleteAll() {
        mapNotNull(Identity<ID>::uuid).deleteAllByIds()
    }

    override fun List<ID>.deleteAllByIds() {
        transaction {
            identityTable.deleteWhere { identityTable.uuid inList this@deleteAllByIds }
        }
    }

    override fun I.save(): I =
        transaction {
            identityTable.upsertReturning {
                it.createDefaultEntity(this@save)
            }.single().let(identityTable::mapToIdentity)
        }

    override fun List<I>.save() =
        transaction {
            identityTable.batchUpsert(this@save) {
                this.createDefaultEntity(it)
            }.map(identityTable::mapToIdentity)
        }


    override fun I.insert(): I {
        val entity = this
        return transaction {
            identityTable.insertReturning {
                it.createDefaultEntity(entity)
            }.singleOrNull()?.let(identityTable::mapToIdentity) ?: entity
        }
    }

    override fun List<I>.insert(): Iterable<I> {
        val entities = this
        return transaction {
            identityTable.batchInsert(entities) {
                this.createDefaultEntity(it)
            }.map(identityTable::mapToIdentity)
        }
    }

    override fun I.update(): I {
        val entity = this
        return transaction {
            identityTable.updateReturning {
                it.createDefaultEntity(entity)
            }.singleOrNull()?.let(identityTable::mapToIdentity) ?: entity
        }
    }

    override fun List<I>.update(): Iterable<I> {
        return transaction {
            identityTable.batchReplace(this@update) {
                this.createDefaultEntity(it)
            }.map(identityTable::mapToIdentity)
        }
    }

    override fun <T> executeInTransaction(transactionCommand: TransactionCommand<T>) =
        transaction {
            transactionCommand()
        }

    override fun I.saveIgnore() {
        val entity = this
        transaction {
            identityTable.upsertReturning {
                it.createDefaultEntity(entity)
            }
        }
    }

    override fun List<I>.saveIgnore() {
        val entities = this
        transaction {
            identityTable.batchUpsert(entities) {
                this.createDefaultEntity(it)
            }
        }
    }

    override fun I.insertIgnore() {
        val entity = this
        transaction {
            identityTable.insert {
                it.createDefaultEntity(entity)
            }
        }
    }

    override fun List<I>.insertIgnore() {
        val entities = this
        transaction {
            identityTable.batchInsert(entities) {
                this.createDefaultEntity(it)
            }
        }
    }

    override fun I.updateIgnore() {
        val entity = this
        transaction {
            identityTable.update {
                it.createDefaultEntity(entity)
            }
        }
    }

    override fun List<I>.updateIgnore() {
        val entities = this
        return transaction {
            identityTable.batchReplace(entities) {
                this.createDefaultEntity(it)
            }
        }
    }

    private fun <K : Any> UpdateBuilder<K>.createDefaultEntity(it: I) {
        it.uuid?.also {
            this[identityTable.uuid] = it
        }
        this[identityTable.creationDate] = it.creationDate
        this.createEntity(it)
    }
}
