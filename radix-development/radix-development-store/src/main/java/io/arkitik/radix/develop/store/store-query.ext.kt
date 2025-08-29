package io.arkitik.radix.develop.store

import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.query.StoreQuery
import java.io.Serializable

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */

infix fun <ID : Serializable, I : Identity<ID>> StoreQuery<ID, I>.find(id: ID) =
    find(id)

infix fun <ID : Serializable, I : Identity<ID>> StoreQuery<ID, I>.find(id: () -> ID) =
    find(id())

infix fun <ID : Serializable, I : Identity<ID>> StoreQuery<ID, I>.notExists(id: ID) =
    !exist(id)

infix fun <ID : Serializable, I : Identity<ID>> StoreQuery<ID, I>.exists(id: ID) =
    exist(id)

infix fun <ID : Serializable, I : Identity<ID>> StoreQuery<ID, I>.exists(id: () -> ID) =
    exist(id())

infix fun <ID : Serializable, I : Identity<ID>> StoreQuery<ID, I>.all(request: PageableData) =
    all(request.page, request.size)

infix fun <ID : Serializable, I : Identity<ID>> StoreQuery<ID, I>.all(request: () -> PageableData) =
    this all request()

infix fun <ID : Serializable, I : Identity<ID>> StoreQuery<ID, I>.allByUuids(ids: List<ID>) =
    allByUuids(ids)


data class PageableData(
    val page: Int,
    val size: Int,
)
