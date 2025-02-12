package io.arkitik.radix.starter.exposed.function

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.exposedLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transactionManager
import org.springframework.transaction.TransactionDefinition
import org.springframework.transaction.TransactionSystemException
import org.springframework.transaction.support.AbstractPlatformTransactionManager
import org.springframework.transaction.support.DefaultTransactionStatus
import org.springframework.transaction.support.SmartTransactionObject

/**
 * @see org.jetbrains.exposed.spring.SpringTransactionManager
 *
 *
 * @author Ibrahim Al-Tamimi
 * @since **19:09, 26/01/2025**
 */
class RadixSpringTransactionManager(
    database: Database,
    databaseConfig: DatabaseConfig = DatabaseConfig {},
    private val showSql: Boolean = false,
) : AbstractPlatformTransactionManager() {

    private var _transactionManager: TransactionManager = database.transactionManager

    private val threadLocalTransactionManager: TransactionManager
        get() = _transactionManager

    init {
        isNestedTransactionAllowed = databaseConfig.useNestedTransactions
    }

    /**
     * ExposedConnection implements savepoint by itself
     * `useSavepointForNestedTransaction` is use `SavepointManager` for nested transaction
     *
     * So we don't need to use java savepoint for nested transaction
     */
    override fun useSavepointForNestedTransaction() = false

    override fun doGetTransaction(): Any {
        val outerManager = TransactionManager.manager
        val outer = threadLocalTransactionManager.currentOrNull()

        return ExposedTransactionObject(
            manager = threadLocalTransactionManager,
            outerManager = outerManager,
            outerTransaction = outer,
        )
    }

    override fun doSuspend(transaction: Any): Any {
        val trxObject = transaction as ExposedTransactionObject
        val currentManager = trxObject.manager

        return SuspendedObject(
            transaction = currentManager.currentOrNull() as Transaction,
            manager = currentManager,
        ).apply {
            currentManager.bindTransactionToThread(null)
            TransactionManager.resetCurrent(null)
        }
    }

    override fun doResume(transaction: Any?, suspendedResources: Any) {
        val suspendedObject = suspendedResources as SuspendedObject

        TransactionManager.resetCurrent(suspendedObject.manager)
        threadLocalTransactionManager.bindTransactionToThread(suspendedObject.transaction)
    }

    private data class SuspendedObject(
        val transaction: Transaction,
        val manager: TransactionManager,
    )

    override fun isExistingTransaction(transaction: Any): Boolean {
        val trxObject = transaction as ExposedTransactionObject
        return trxObject.getCurrentTransaction() != null
    }

    override fun doBegin(transaction: Any, definition: TransactionDefinition) {
        val trxObject = transaction as ExposedTransactionObject

        val currentTransactionManager = trxObject.manager
        TransactionManager.resetCurrent(threadLocalTransactionManager)

        currentTransactionManager.newTransaction(
            isolation = definition.isolationLevel,
            readOnly = definition.isReadOnly,
            outerTransaction = currentTransactionManager.currentOrNull()
        ).apply {
            if (definition.timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
                queryTimeout = definition.timeout
            }

            if (showSql) {
                addLogger(StdOutSqlLogger)
            }
        }
    }

    override fun doCommit(status: DefaultTransactionStatus) {
        val trxObject = status.transaction as ExposedTransactionObject
        TransactionManager.resetCurrent(trxObject.manager)
        trxObject.commit()
    }

    override fun doRollback(status: DefaultTransactionStatus) {
        val trxObject = status.transaction as ExposedTransactionObject
        TransactionManager.resetCurrent(trxObject.manager)
        trxObject.rollback()
    }

    override fun doCleanupAfterCompletion(transaction: Any) {
        val trxObject = transaction as ExposedTransactionObject

        trxObject.cleanUpTransactionIfIsPossible {
            closeStatementsAndConnections(it)
        }

        trxObject.setCurrentToOuter()
    }

    private fun closeStatementsAndConnections(transaction: Transaction) {
        val currentStatement = transaction.currentStatement
        @Suppress("TooGenericExceptionCaught")
        try {
            currentStatement?.let {
                it.closeIfPossible()
                transaction.currentStatement = null
            }
            transaction.closeExecutedStatements()
        } catch (error: Exception) {
            exposedLogger.warn("Statements close failed", error)
        }

        @Suppress("TooGenericExceptionCaught")
        try {
            transaction.close()
        } catch (error: Exception) {
            exposedLogger.warn("Transaction close failed: ${error.message}. Statement: $currentStatement", error)
        }
    }

    override fun doSetRollbackOnly(status: DefaultTransactionStatus) {
        val trxObject = status.transaction as ExposedTransactionObject
        trxObject.setRollbackOnly()
    }

    private data class ExposedTransactionObject(
        val manager: TransactionManager,
        val outerManager: TransactionManager,
        private val outerTransaction: Transaction?,
    ) : SmartTransactionObject {

        private var isRollback: Boolean = false

        fun cleanUpTransactionIfIsPossible(block: (transaction: Transaction) -> Unit) {
            val currentTransaction = getCurrentTransaction()
            if (currentTransaction != null) {
                block(currentTransaction)
            }
        }

        fun setCurrentToOuter() {
            manager.bindTransactionToThread(outerTransaction)
            TransactionManager.resetCurrent(outerManager)
        }

        @Suppress("TooGenericExceptionCaught")
        fun commit() {
            try {
                manager.currentOrNull()?.commit()
            } catch (error: Exception) {
                throw TransactionSystemException(error.message.orEmpty(), error)
            }
        }

        @Suppress("TooGenericExceptionCaught")
        fun rollback() {
            try {
                manager.currentOrNull()?.rollback()
            } catch (error: Exception) {
                throw TransactionSystemException(error.message.orEmpty(), error)
            }
        }

        fun getCurrentTransaction(): Transaction? = manager.currentOrNull()

        fun setRollbackOnly() {
            isRollback = true
        }

        override fun isRollbackOnly() = isRollback

        override fun flush() {
            // Do noting
        }
    }
}
