package io.arkitik.radix.develop.usecase

import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
@FunctionalInterface
interface FunctionalUseCase<RQ : UseCaseRequest, RS : UseCaseResponse> {
    fun RQ.process(): RS
}