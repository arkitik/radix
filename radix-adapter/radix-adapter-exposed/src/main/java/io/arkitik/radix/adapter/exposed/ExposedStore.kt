package io.arkitik.radix.adapter.exposed

import io.arkitik.radix.adapter.exposed.query.ExposedStoreQuery
import io.arkitik.radix.develop.exposed.table.RadixTable
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.Store
import io.arkitik.radix.develop.store.TransactionCommand
import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.radix.develop.store.updateIgnore
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsert
import java.io.Serializable

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:36 PM, 26/06/2024
 */
abstract class ExposedStore<ID, I : Identity<ID>, IT : RadixTable<ID, I>>(
    protected val identityTable: IT,
    protected val database: Database? = null,
) : Store<ID, I> where ID : Serializable, ID : Comparable<ID> {

    protected open val batchDeletion: Boolean = true
    protected open val batchSize: Int = 1000

    protected abstract fun <K : Any> UpdateBuilder<K>.createEntity(identity: I)
    protected open fun <K : Any> UpdateBuilder<K>.updateEntity(identity: I) = createEntity(identity)

    override val storeQuery: StoreQuery<ID, I>
        get() = ExposedStoreQuery(
            identityTable = identityTable,
            database = database
        )

    private fun <T> batchProcess(items: List<T>, processBatch: (chunk: List<T>) -> Unit) =
        if (batchDeletion)
            items.chunked(batchSize)
                .forEach { batch ->
                    processBatch(batch)
                }
        else processBatch(items)

    override fun I.delete() {
        this.uuid!!.delete()
    }

    override fun ID.delete() {
        val recordUuid = this
        transaction(database) {
            identityTable.deleteWhere { identityTable.uuid eq recordUuid }
        }
    }

    override fun List<I>.deleteAll() {
        mapNotNull(Identity<ID>::uuid).deleteAllByIds()
    }

    override fun List<ID>.deleteAllByIds() {
        val recordUuids = this
        transaction(database) {
            batchProcess(recordUuids) { chunk ->
                identityTable.deleteWhere { identityTable.uuid inList chunk }
            }
        }
    }

    @Deprecated(
        "This method will be removed in a future version. Use 'update()' for modifying existing records or 'insert()' for creating new ones.",
        replaceWith = ReplaceWith("insert() or update()"),
        level = DeprecationLevel.WARNING
    )
    override fun I.save(): I {
        return transaction(database) {
            identityTable.upsert {
                it.createDefaultEntity(this@save)
            }
            identityTable.findIdentityByUuid(uuid!!, database)!!
        }
    }

    @Deprecated(
        "This method will be removed in a future version. Use 'update()' for modifying existing records or 'insert()' for creating new ones.",
        replaceWith = ReplaceWith("insert() or update()"),
        level = DeprecationLevel.WARNING
    )
    override fun List<I>.save() =
        transaction(database) {
            identityTable.batchUpsert(this@save) {
                this.createDefaultEntity(it)
            }.map { resultRow ->
                identityTable.mapToIdentity(resultRow, database)
            }
        }


    override fun I.insert(): I {
        val recordToBeInserted = this
        return transaction(database) {
            identityTable.insert { rowItem ->
                rowItem.createDefaultEntity(recordToBeInserted)
            }
            recordToBeInserted
        }
    }

    override fun List<I>.insert(): Iterable<I> {
        val entities = this
        return transaction(database) {
            identityTable.batchInsert(entities) { rowItem ->
                this.createDefaultEntity(rowItem)
            }.map { resultRow ->
                identityTable.mapToIdentity(resultRow, database)
            }
        }
    }

    override fun I.update(): I {
        val entity = this
        return transaction(database) {
            identityTable.update(
                where = {
                    identityTable.uuid.eq(entity.uuid!!)
                }
            ) {
                it.updateDefaultEntity(entity)
            }
            entity
        }
    }

    override fun List<I>.update(): Iterable<I> {
        val entities = this
        transaction(database) {
            entities.forEach { entity ->
                identityTable.update(where = {
                    identityTable.uuid.eq(entity.uuid!!)
                }) {
                    it.updateDefaultEntity(entity)
                }
            }
        }
        return entities
    }

    override fun <T> executeInTransaction(transactionCommand: TransactionCommand<T>) =
        transaction(database) {
            transactionCommand()
        }

    @Deprecated(
        "This method will be removed in a future version. Use 'updateIgnore()' for modifying existing records or 'insertIgnore()' for creating new ones.",
        replaceWith = ReplaceWith("insertIgnore() or updateIgnore()"),
        level = DeprecationLevel.WARNING
    )
    override fun I.saveIgnore() {
        val entity = this
        transaction(database) {
            identityTable.upsert {
                it.createDefaultEntity(entity)
            }
        }
    }

    @Deprecated(
        "This method will be removed in a future version. Use 'updateIgnore()' for modifying existing records or 'insertIgnore()' for creating new ones.",
        replaceWith = ReplaceWith("insertIgnore() or updateIgnore()"),
        level = DeprecationLevel.WARNING
    )
    override fun List<I>.saveIgnore() {
        val entities = this
        transaction(database) {
            identityTable.batchUpsert(entities) {
                this.createDefaultEntity(it)
            }
        }
    }

    override fun I.insertIgnore() {
        val entity = this
        transaction(database) {
            identityTable.insert {
                it.createDefaultEntity(entity)
            }
        }
    }

    override fun List<I>.insertIgnore() {
        val entities = this
        transaction(database) {
            identityTable.batchInsert(entities) {
                this.createDefaultEntity(it)
            }
        }
    }

    override fun I.updateIgnore() {
        val entity = this
        transaction(database) {
            identityTable.update(where = {
                identityTable.uuid.eq(entity.uuid!!)
            }) {
                it.updateDefaultEntity(entity)
            }
        }
    }

    override fun List<I>.updateIgnore() {
        val entities = this
        transaction(database) {
            entities.forEach(::updateIgnore)
        }
    }

    private fun <K : Any> UpdateBuilder<K>.createDefaultEntity(rowItem: I) {
        rowItem.uuid?.also {
            this[identityTable.uuid] = it
        }
        this[identityTable.creationDate] = rowItem.creationDate
        this.createEntity(rowItem)
    }

    private fun <K : Any> UpdateBuilder<K>.updateDefaultEntity(rowItem: I) {
        this.updateEntity(rowItem)
    }
}
