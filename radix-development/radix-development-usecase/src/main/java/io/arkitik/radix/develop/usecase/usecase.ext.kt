package io.arkitik.radix.develop.usecase

import io.arkitik.radix.develop.usecase.factory.UseCaseFactory
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */

infix fun <RQ : UseCaseRequest, RS : UseCaseResponse> FunctionalUseCase<RQ, RS>.process(request: RQ) =
    request.process()

infix fun <RQ : UseCaseRequest, RS : UseCaseResponse> FunctionalUseCase<RQ, RS>.process(requestProvider: () -> RQ) =
    this process requestProvider()

infix fun <RQ : UseCaseRequest> CommandUseCase<RQ>.execute(request: RQ) =
    request.execute()

infix fun <RQ : UseCaseRequest> CommandUseCase<RQ>.execute(requestProvider: () -> RQ) =
    this execute requestProvider()

infix fun <F : UseCaseFactory, RQ : UseCaseRequest, RS : UseCaseResponse> F.functional(functionalUseCase: F.() -> FunctionalUseCase<RQ, RS>) =
    functionalUseCase()

infix fun <F : UseCaseFactory, RQ : UseCaseRequest> F.command(commandUseCase: F.() -> CommandUseCase<RQ>) =
    commandUseCase()
