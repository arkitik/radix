package io.arkitik.radix.develop.usecase.validation.command

import io.arkitik.radix.develop.usecase.action.Actionable
import io.arkitik.radix.develop.usecase.adapter.RequestAdapter
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.validation.func.DefaultUseCaseValidator
import io.arkitik.radix.develop.usecase.validation.func.UseCaseValidator
import io.arkitik.radix.develop.usecase.validation.validate
import io.arkitik.radix.usecase.reactive.command.ReactiveMonoCommandUseCase
import reactor.core.publisher.Mono

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 12, **Fri February, 2021**
 * Project *radix* [https://arkitik.io]
 */
abstract class ValidationReactiveMonoCommandUseCase<RQ : UseCaseRequest>(
    private val validator: UseCaseValidator = DefaultUseCaseValidator.create(),
) : ReactiveMonoCommandUseCase<RQ>, Actionable<RQ, Unit> {
    final override fun RQ.before() = validator validate this

    override fun RQ.after(response: Unit) = Unit
    final override fun RequestAdapter<Mono<RQ>>.execute() =
        with(request) {
            this.map {
                it.apply { before() }
            }.doExecute()
        }

    abstract fun Mono<RQ>.doExecute()
}