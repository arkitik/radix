package io.arkitik.radix.develop.usecase.adapter

import io.arkitik.radix.develop.usecase.CommandUseCase
import io.arkitik.radix.develop.usecase.FunctionalUseCase
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 2:20 PM, 25 , **Sat, June 2022**
 * Project *radix* [arkitik.io](https://arkitik.io)
 */
interface AdapterCommandUseCase<RQ> : CommandUseCase<RequestAdapter<RQ>>

interface ResponseAdapterFunctionalUseCase<RQ : UseCaseRequest, RS> : FunctionalUseCase<RQ, ResponseAdapter<RS>>
interface RequestAdapterFunctionalUseCase<RQ, RS : UseCaseResponse> : FunctionalUseCase<RequestAdapter<RQ>, RS>

interface AdapterFunctionalUseCase<RQ, RS> : ResponseAdapterFunctionalUseCase<RequestAdapter<RQ>, RS>
