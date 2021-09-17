package io.arkitik.radix.develop.usecase.validation.command

import io.arkitik.radix.develop.usecase.action.Actionable
import io.arkitik.radix.develop.usecase.adapter.RequestAdapter
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.validation.func.DefaultUseCaseValidator
import io.arkitik.radix.develop.usecase.validation.func.UseCaseValidator
import io.arkitik.radix.develop.usecase.validation.validate
import io.arkitik.radix.usecase.reactive.command.ReactiveFluxCommandUseCase
import reactor.core.publisher.Flux

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 12, **Fri February, 2021**
 * Project *radix* [https://arkitik.io]
 */
abstract class ValidationReactiveFluxCommandUseCase<RQ : UseCaseRequest>(
    private val validator: UseCaseValidator = DefaultUseCaseValidator.create(),
) : ReactiveFluxCommandUseCase<RQ>, Actionable<RQ, Unit> {
    final override fun RQ.before() = validator validate this

    override fun RQ.after(response: Unit) = Unit
    final override fun RequestAdapter<Flux<RQ>>.execute() {
        with(request) {
            map {
                it.apply { before() }
            }.doExecute()
        }
    }

    abstract fun Flux<RQ>.doExecute()
}