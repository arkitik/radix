package io.arkitik.radix.develop.store

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.creator.StoreIdentityCreator
import io.arkitik.radix.develop.store.query.StoreQuery
import io.arkitik.radix.develop.store.updater.StoreIdentityUpdater
import java.io.Serializable

/**
 * Represents a command that can be executed within a transaction.
 *
 * This is a typealias for a function that takes no arguments and returns a value of type `T`.
 * It is typically used to encapsulate operations that need to be executed atomically within a transactional context.
 *
 * @param T The type of the result returned by the command.
 *
 * Example usage:
 * ```
 * val command: TransactionCommand<String> = {
 *     // Perform transactional operations
 *     "Transaction completed successfully"
 * }
 * ```
 */
typealias TransactionCommand<T> = () -> T

/**
 * A generic interface for managing entities in a store, where each entity is identified by a unique ID.
 *
 * @param ID The type of the unique identifier for the entities. This type must be serializable.
 * @param I The type of the entity, which must implement the [Identity] interface with the corresponding ID type.
 *
 * This interface provides methods for CRUD operations (Create, Read, Update, Delete) and querying entities.
 * It is designed to work with any entity type that has a serializable identifier.
 *
 * @see Identity
 * @author Ibrahim Al-Tamimi
 * @since 29, **Thu Oct, 2020**
 */
interface Store<ID : Serializable, I : Identity<ID>> {
    /**
     * Deletes the entity associated with the given ID.
     */
    fun ID.delete()

    /**
     * Deletes the current identity instance.
     */
    fun I.delete()

    /**
     * Deletes all entities associated with the provided list of IDs.
     */
    fun List<ID>.deleteAllByIds()

    /**
     * Deletes all entities in the provided list of identity instances.
     */
    fun List<I>.deleteAll()

    /**
     * Provides a query interface for interacting with the store.
     *
     * @return A [StoreQuery] instance for querying entities of type `I` using IDs of type `ID`.
     */
    val storeQuery: StoreQuery<ID, I>

    /**
     * Creates a new identity instance.
     *
     * @return A [StoreIdentityCreator] instance for creating new entities of type `I` with IDs of type `ID`.
     */
    fun identityCreator(): StoreIdentityCreator<ID, I>

    /**
     * Updates the identity of the current instance.
     *
     * @return A [StoreIdentityUpdater] instance for updating the identity of the current entity of type `I` with IDs of type `ID`.
     */
    fun I.identityUpdater(): StoreIdentityUpdater<ID, I>

    /**
     * Saves the current identity instance. This method is deprecated and will be removed in a future version.
     *
     * @return The saved instance of type `I`.
     *
     * @deprecated This method is no longer recommended for use. It will be removed in a future version.
     *             Use `update()` if you are modifying an existing record or `insert()` if you are creating a new one.
     * @see update
     * @see insert
     */
    @Deprecated(
        message = "This method will be removed in a future version. Use 'update()' for modifying existing records or 'insert()' for creating new ones.",
        replaceWith = ReplaceWith(
            expression = "insert() or update()",
        ),
        level = DeprecationLevel.WARNING
    )
    fun I.save(): I

    /**
     * Saves a list of identity instances. This method is deprecated and will be removed in a future version.
     *
     * @return The saved instances as an iterable of type `I`.
     *
     * @deprecated This method is no longer recommended for use. It will be removed in a future version.
     *             Use `update()` if you are modifying existing records or `insert()` if you are creating new ones.
     * @see update
     * @see insert
     */
    @Deprecated(
        message = "This method will be removed in a future version. Use 'update()' for modifying existing records or 'insert()' for creating new ones.",
        replaceWith = ReplaceWith(
            expression = "insert() or update()",
        ),
        level = DeprecationLevel.WARNING
    )
    fun List<I>.save(): Iterable<I>

    /**
     * Inserts the current identity instance as a new record.
     *
     * @return The inserted instance of type `I`.
     */
    fun I.insert(): I

    /**
     * Inserts a list of identity instances as new records.
     *
     * @return The inserted instances as an iterable of type `I`.
     */
    fun List<I>.insert(): Iterable<I>

    /**
     * Updates the current identity instance as an existing record.
     *
     * @return The updated instance of type `I`.
     */
    fun I.update(): I

    /**
     * Updates a list of identity instances as existing records.
     *
     * @return The updated instances as an iterable of type `I`.
     */
    fun List<I>.update(): Iterable<I>

    /**
     * Saves the current identity instance.
     * This method is deprecated and will be removed in a future version.
     *
     * @deprecated This method is no longer recommended for use. It will be removed in a future version.
     *             Use `updateIgnore()` if you are modifying an existing record or `insertIgnore()` if you are creating a new one.
     * @see updateIgnore
     * @see insertIgnore
     */
    @Deprecated(
        message = "This method will be removed in a future version. Use 'updateIgnore()' for modifying existing records or 'insertIgnore()' for creating new ones.",
        replaceWith = ReplaceWith(
            expression = "insertIgnore() or updateIgnore()",
        ),
        level = DeprecationLevel.WARNING
    )
    fun I.saveIgnore()

    /**
     * Saves a list of identity instances.
     * This method is deprecated and will be removed in a future version.
     *
     * @deprecated This method is no longer recommended for use. It will be removed in a future version.
     *             Use `updateIgnore()` if you are modifying existing records or `insertIgnore()` if you are creating new ones.
     * @see updateIgnore
     * @see insertIgnore
     */
    @Deprecated(
        message = "This method will be removed in a future version. Use 'updateIgnore()' for modifying existing records or 'insertIgnore()' for creating new ones.",
        replaceWith = ReplaceWith(
            expression = "insertIgnore() or updateIgnore()",
        ),
        level = DeprecationLevel.WARNING
    )
    fun List<I>.saveIgnore()

    /**
     * Inserts the current identity instance as a new record.
     */
    fun I.insertIgnore()

    /**
     * Inserts a list of identity instances as new records.
     */
    fun List<I>.insertIgnore()

    /**
     * Updates the current identity instance as an existing record.
     */
    fun I.updateIgnore()

    /**
     * Updates a list of identity instances as existing records.
     */
    fun List<I>.updateIgnore()

    fun <T> executeInTransaction(transactionCommand: TransactionCommand<T>): T =
        transactionCommand()
}
