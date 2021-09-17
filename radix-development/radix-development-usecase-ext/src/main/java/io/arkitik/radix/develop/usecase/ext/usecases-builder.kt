package io.arkitik.radix.develop.usecase.ext

import io.arkitik.radix.develop.usecase.CommandUseCase
import io.arkitik.radix.develop.usecase.FunctionalUseCase
import io.arkitik.radix.develop.usecase.action.ActionableCommandUseCase
import io.arkitik.radix.develop.usecase.action.ActionableFunctionalUseCase
import io.arkitik.radix.develop.usecase.factory.UseCaseFactory
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import io.arkitik.radix.develop.usecase.model.UseCaseResponse

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 28, **Sat November, 2020**
 * Project *radix* [https://arkitik.io]
 */

typealias UseCaseRole<RQ, RS> = RQ.() -> RS
typealias UseCaseOperator<RQ, RS> = RQ.() -> RS

fun <F : UseCaseFactory, RQ : UseCaseRequest> F.commandUseCaseBuilder(
    command: UseCaseOperator<RQ, Unit>,
    builder: BuilderCommandUseCase<RQ>.(F) -> Unit,
): CommandUseCase<RQ> =
    BuilderCommandUseCase(command).apply {
        builder(this@commandUseCaseBuilder)
    }

infix fun <RQ : UseCaseRequest> UseCaseOperator<RQ, Unit>.commandUseCaseBuilder(
    builder: BuilderCommandUseCase<RQ>.() -> Unit,
): CommandUseCase<RQ> =
    BuilderCommandUseCase(this).apply {
        builder()
    }

fun <F : UseCaseFactory, RQ : UseCaseRequest, RS : UseCaseResponse> F.functionalUseCaseBuilder(
    function: UseCaseOperator<RQ, RS>,
    builder: BuilderFunctionalUseCase<RQ, RS>.(F) -> Unit,
): FunctionalUseCase<RQ, RS> =
    BuilderFunctionalUseCase(function).apply {
        builder(this@functionalUseCaseBuilder)
    }

infix fun <RQ : UseCaseRequest, RS : UseCaseResponse> UseCaseOperator<RQ, RS>.functionalUseCaseBuilder(
    builder: BuilderFunctionalUseCase<RQ, RS>.() -> Unit,
): FunctionalUseCase<RQ, RS> =
    BuilderFunctionalUseCase(this).apply {
        builder()
    }

class BuilderCommandUseCase<RQ : UseCaseRequest>(
    private val command: UseCaseOperator<RQ, Unit>,
) : ActionableCommandUseCase<RQ>() {
    private val afterOperators: MutableList<UseCaseOperator<RQ, Unit>> = mutableListOf()
    private val roles: MutableList<UseCaseRole<RQ, Unit>> = mutableListOf()
    override fun RQ.before() =
        roles.forEach {
            it()
        }

    override fun RQ.doExecute() = command()
    override fun RQ.after(response: Unit) = afterOperators.forEach { it() }

    infix fun afterCommand(operator: UseCaseOperator<RQ, Unit>) {
        this.afterOperators.add(operator)
    }

    infix fun install(role: UseCaseRole<RQ, Unit>) {
        roles.add(role)
    }
}

class BuilderFunctionalUseCase<RQ : UseCaseRequest, RS : UseCaseResponse>(
    private val function: UseCaseOperator<RQ, RS>,
) : ActionableFunctionalUseCase<RQ, RS>() {
    private val roles: MutableList<UseCaseRole<RQ, Unit>> = mutableListOf()
    private val afterOperators: MutableList<UseCaseOperator<RQ, RS>> = mutableListOf()

    override fun RQ.before() = roles.forEach { it() }

    override fun RQ.doProcess() = function()

    override fun RQ.after(response: RS) = afterOperators.forEach { it() }

    infix fun afterFunction(operator: UseCaseOperator<RQ, RS>) {
        this.afterOperators.add(operator)
    }

    infix fun install(role: UseCaseRole<RQ, Unit>) {
        roles.add(role)
    }
}