package io.arkitik.radix.develop.exposed.table

import org.jetbrains.exposed.v1.core.Transaction
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.transactions.TransactionManager
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

/**
 * Executes the given [statement] inside a transaction.
 * If already in a transaction for the same database, it reuses it; otherwise, starts a new one.
 */
inline fun <T> ensureInTransaction(
    database: Database? = null,
    crossinline statement: Transaction.() -> T,
): T {
    val currentTransaction = TransactionManager.currentOrNull()
    return if (currentTransaction != null && currentTransaction.db == database) {
        statement(currentTransaction)
    } else {
        transaction(database) {
            statement(this)
        }
    }
}
