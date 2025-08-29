package io.arkitik.radix.develop.store

import io.arkitik.radix.develop.identity.Identity
import java.io.Serializable

/**
 *
 * @author Ibrahim Al-Tamimi
 * @since **16:38, 10/02/2025**
 */
@Deprecated(
    message = "This method will be removed in a future version. Use 'update()' for modifying existing records or 'insert()' for creating new ones.",
    replaceWith = ReplaceWith(
        expression = "insert() or update()",
    ),
    level = DeprecationLevel.WARNING
)
infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.save(identity: I) = identity.save()

@Deprecated(
    message = "This method will be removed in a future version. Use 'update()' for modifying existing records or 'insert()' for creating new ones.",
    replaceWith = ReplaceWith(
        expression = "insert() or update()",
    ),
    level = DeprecationLevel.WARNING
)
infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.save(identities: List<I>) = identities.save()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.insert(identity: I) = identity.insert()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.insert(identities: List<I>) = identities.insert()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.update(identity: I) = identity.update()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.update(identities: List<I>) = identities.update()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.insertIgnore(identity: I) = identity.insertIgnore()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.insertIgnore(identities: List<I>) =
    identities.insertIgnore()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.updateIgnore(identity: I) = identity.updateIgnore()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.updateIgnore(identities: List<I>) =
    identities.updateIgnore()
