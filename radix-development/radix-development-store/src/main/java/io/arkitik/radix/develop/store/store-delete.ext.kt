package io.arkitik.radix.develop.store

import io.arkitik.radix.develop.identity.Identity
import java.io.Serializable

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.delete(identity: I) = identity.delete()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.deleteId(id: ID) = id.delete()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.delete(identities: List<I>) = identities.deleteAll()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.deleteIds(ids: List<ID>) = ids.deleteAllByIds()
