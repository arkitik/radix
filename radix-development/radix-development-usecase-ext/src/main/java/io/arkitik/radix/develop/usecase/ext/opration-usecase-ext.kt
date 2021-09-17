package io.arkitik.radix.develop.usecase.ext

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.usecase.CommandUseCase
import io.arkitik.radix.develop.usecase.FunctionalUseCase
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28, **Sun February, 2021**
 * Project *radix* [https://arkitik.io]
 */
fun <RQ : UseCaseRequest, RS : UseCaseResponse> FunctionalUseCase<RQ, RS>.toOperation(): Operation<RQ, RS> =
    UseCaseOperation {
        process()
    }

fun <RQ : UseCaseRequest> CommandUseCase<RQ>.toOperation(): Operation<RQ, Unit> =
    UseCaseOperation {
        execute()
    }

private class UseCaseOperation<RQ, RS>(
    private val function: RQ.() -> RS,
) : Operation<RQ, RS> {
    override fun RQ.operate() = function()
}