package io.arkitik.radix.usecase.reactive.command

import io.arkitik.radix.develop.usecase.adapter.adapterExecute
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 12, **Fri February, 2021**
 * Project *radix* [https://arkitik.io]
 */
infix fun <RQ : UseCaseRequest> ReactiveFluxCommandUseCase<RQ>.execute(request: RQ) =
    adapterExecute(listOf(request).toFlux())

infix fun <RQ : UseCaseRequest> ReactiveMonoCommandUseCase<RQ>.execute(request: RQ) =
    adapterExecute(request.toMono())