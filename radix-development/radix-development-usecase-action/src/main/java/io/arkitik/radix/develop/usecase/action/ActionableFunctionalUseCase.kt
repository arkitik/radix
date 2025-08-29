package io.arkitik.radix.develop.usecase.action

import io.arkitik.radix.develop.usecase.FunctionalUseCase
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
abstract class ActionableFunctionalUseCase<RQ : UseCaseRequest, RS : UseCaseResponse> : FunctionalUseCase<RQ, RS>,
    Actionable<RQ, RS> {
    final override fun RQ.process() =
        with(this) {
            before()
            doProcess().also {
                after(it)
            }
        }

    abstract fun RQ.doProcess(): RS
}
