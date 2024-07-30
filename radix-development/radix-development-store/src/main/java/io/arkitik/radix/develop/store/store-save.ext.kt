package io.arkitik.radix.develop.store

import io.arkitik.radix.develop.identity.Identity
import java.io.Serializable

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.save(identity: I) = identity.save()

infix fun <ID : Serializable, I : Identity<ID>> Store<ID, I>.save(identities: List<I>) = identities.save()
