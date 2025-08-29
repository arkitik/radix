package io.arkitik.radix.develop.usecase.validation.functional

import io.arkitik.radix.develop.usecase.action.ActionableFunctionalUseCase
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse
import io.arkitik.radix.develop.usecase.validation.func.DefaultUseCaseValidator
import io.arkitik.radix.develop.usecase.validation.func.UseCaseValidator
import io.arkitik.radix.develop.usecase.validation.validate

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
abstract class ValidationFunctionalUseCase<RQ : UseCaseRequest, RS : UseCaseResponse>(
    private val validator: UseCaseValidator = DefaultUseCaseValidator(),
) : ActionableFunctionalUseCase<RQ, RS>() {

    final override fun RQ.before() = (validator validate this).also {
        doBefore()
    }

    override fun RQ.after(response: RS) {
        // DO NOTHING
    }

    open fun RQ.doBefore() {

    }
}
