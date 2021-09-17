package io.arkitik.radix.usecase.reactive.command

import io.arkitik.radix.develop.usecase.CommandUseCase
import io.arkitik.radix.develop.usecase.adapter.RequestAdapter
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import reactor.core.publisher.Flux

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 12, **Fri February, 2021**
 * Project *radix* [https://arkitik.io]
 */
interface ReactiveFluxCommandUseCase<RQ : UseCaseRequest> : CommandUseCase<RequestAdapter<Flux<RQ>>>