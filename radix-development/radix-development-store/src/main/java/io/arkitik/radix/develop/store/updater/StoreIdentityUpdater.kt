package io.arkitik.radix.develop.store.updater

import io.arkitik.radix.develop.identity.Identity
import java.io.Serializable

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
interface StoreIdentityUpdater<ID : Serializable, I : Identity<ID>> {
    fun update(): I
}
