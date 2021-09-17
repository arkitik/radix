package io.arkitik.radix.usecase.reactive.functional

import io.arkitik.radix.develop.usecase.FunctionalUseCase
import io.arkitik.radix.develop.usecase.adapter.RequestAdapter
import io.arkitik.radix.develop.usecase.adapter.ResponseAdapter
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse
import reactor.core.publisher.Mono

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 12, **Fri February, 2021**
 * Project *radix* [https://arkitik.io]
 */
interface ReactiveMonoFunctionalUseCase<RQ : UseCaseRequest, RS : UseCaseResponse> :
    FunctionalUseCase<RequestAdapter<Mono<RQ>>, ResponseAdapter<Mono<RS>>>