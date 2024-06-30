package io.arkitik.radix.develop.exposed.table

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 12:05 PM, 30/06/2024
 */
abstract class RadixTable<ID : Comparable<ID>>(name: String = "") : Table(name) {
    abstract val uuid: Column<ID>
    val creationDate = datetime(name = "creation_date")

    override val primaryKey: PrimaryKey
        get() = PrimaryKey(uuid)
}

