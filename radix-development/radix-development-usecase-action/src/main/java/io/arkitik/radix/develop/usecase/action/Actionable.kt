package io.arkitik.radix.develop.usecase.action

import io.arkitik.radix.develop.usecase.model.UseCaseRequest

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
interface Actionable<RQ : UseCaseRequest, RS> {
    fun RQ.before()
    fun RQ.after(response: RS)
}
