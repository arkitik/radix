package io.arkitik.radix.develop.usecase.adapter

import io.arkitik.radix.develop.usecase.*
import io.arkitik.radix.develop.usecase.factory.UseCaseFactory
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 07, **Sat Nov, 2020**
 * Project **radix** [https://arkitik.io](https://arkitik.io)
 */
infix fun <RQ, RS> FunctionalUseCase<RequestAdapter<RQ>, ResponseAdapter<RS>>.adapterProcess(request: RQ) =
    (this process RequestAdapter(request)).response

infix fun <RQ, RS : UseCaseResponse> FunctionalUseCase<RequestAdapter<RQ>, RS>.adapterProcess(request: RQ) =
    this process RequestAdapter(request)

infix fun <RQ : UseCaseRequest, RS> FunctionalUseCase<RQ, ResponseAdapter<RS>>.adapterProcess(request: RQ) =
    (this process request).response

infix fun <RQ, RS> FunctionalUseCase<RequestAdapter<RQ>, ResponseAdapter<RS>>.adapterProcess(requestProvider: () -> RQ) =
    this adapterProcess requestProvider()

infix fun <RQ, RS : UseCaseResponse> FunctionalUseCase<RequestAdapter<RQ>, RS>.adapterProcess(request: () -> RQ) =
    this adapterProcess request()

infix fun <RQ : UseCaseRequest, D> FunctionalUseCase<RQ, ResponseAdapter<D>>.asResponse(response: D) =
    ResponseAdapter(response)

infix fun <RQ : UseCaseRequest, D> FunctionalUseCase<RQ, ResponseAdapter<D>>.toResponse(response: FunctionalUseCase<RQ, ResponseAdapter<D>>.() -> D) =
    ResponseAdapter(response())

infix fun <RQ> CommandUseCase<RequestAdapter<RQ>>.adapterExecute(request: RQ) =
    this execute RequestAdapter(request)

infix fun <RQ> CommandUseCase<RequestAdapter<RQ>>.adapterExecute(requestProvider: () -> RQ) =
    this adapterExecute requestProvider()

infix fun <F : UseCaseFactory, RQ, RS> F.adapterFunctional(functionalUseCase: F.() -> FunctionalUseCase<RequestAdapter<RQ>, ResponseAdapter<RS>>) =
    this functional functionalUseCase

infix fun <F : UseCaseFactory, RQ : UseCaseRequest, RS> F.adapterFunctionalReq(functionalUseCase: F.() -> FunctionalUseCase<RQ, ResponseAdapter<RS>>) =
    this functional functionalUseCase

infix fun <F : UseCaseFactory, RQ, RS : UseCaseResponse> F.adapterFunctionalResp(functionalUseCase: F.() -> FunctionalUseCase<RequestAdapter<RQ>, RS>) =
    this functional functionalUseCase

infix fun <F : UseCaseFactory, RQ> F.adapterCommand(commandUseCase: F.() -> CommandUseCase<RequestAdapter<RQ>>) =
    this command commandUseCase

