package io.arkitik.radix.develop.exposed.table

import io.arkitik.radix.develop.identity.Identity
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.io.Serializable

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 12:05 PM, 30/06/2024
 */
abstract class RadixTable<ID, I>(
    name: String = "",
) : Table(name) where ID : Serializable, ID : Comparable<ID>, I : Identity<ID> {
    abstract val uuid: Column<ID>
    val creationDate =
        datetime("creation_date").defaultExpression(CurrentDateTime)

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(uuid)

    open fun findIdentityByUuid(uuid: ID, database: Database? = null): I? =
        ensureInTransaction(database) {
            select(columns)
                .where {
                    this@RadixTable.uuid eq uuid
                }.singleOrNull()?.let { resultRow ->
                    mapToIdentity(resultRow, database)
                }
        }

    abstract fun mapToIdentity(resultRow: ResultRow, database: Database? = null): I
}

