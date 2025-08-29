package io.arkitik.radix.adapter.shared.repository

import io.arkitik.radix.develop.identity.Identity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean
import java.io.Serializable

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
@NoRepositoryBean
interface RadixRepository<ID : Serializable, I : Identity<ID>> : CrudRepository<I, ID> {
    fun findByUuid(id: ID): I?

    fun findAll(pageable: Pageable): Page<I>
}
