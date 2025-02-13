package io.arkitik.radix.adapter.shared.query

import io.arkitik.radix.adapter.shared.paged
import io.arkitik.radix.adapter.shared.repository.RadixRepository
import io.arkitik.radix.develop.identity.Identity
import io.arkitik.radix.develop.store.query.PageData
import io.arkitik.radix.develop.store.query.StoreQuery
import org.springframework.data.domain.PageRequest
import java.io.Serializable

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
open class StoreQueryImpl<ID : Serializable, I : Identity<ID>, E : I>(
    private val repository: RadixRepository<ID, E>,
) : StoreQuery<ID, I> {
    override fun find(uuid: ID): I? = repository.findByUuid(uuid)
    override fun exist(uuid: ID): Boolean = repository.existsById(uuid)
    override fun allByUuids(uuids: List<ID>): Iterable<I> = repository.findAllById(uuids)
    override fun all(): List<I> = repository.findAll().toList()
    override fun all(page: Int, size: Int): PageData<I> =
        repository.findAll(PageRequest.of(page, size)).paged()
}
