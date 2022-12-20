package io.arkitik.radix.develop.operation.ext

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.Operator

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 07  1:06 AM, **Fri July, 2021**
 * Project *radix* [https://arkitik.io]
 */
@DslMarker
internal annotation class OptBuilderDsl

@DslMarker
internal annotation class OptMainBuilderDsl

@DslMarker
internal annotation class OptRoleBuilderDsl

@DslMarker
internal annotation class OptOperatorBuilderDsl


@OptBuilderDsl
fun <RQ, RS> operationBuilder(
    builder: OperationBuilder<RQ, RS>.() -> Unit,
) = OperationBuilder<RQ, RS>()
    .apply(builder)
    .build()

@OptBuilderDsl
class OperationBuilder<RQ, RS> {
    private val roles: MutableList<OperationRole<RQ, Unit>> = mutableListOf()
    private val operators: MutableList<Operator<RQ, RS>> = mutableListOf()
    private lateinit var operation: Operation<RQ, RS>

    @OptMainBuilderDsl
    infix fun mainOperation(operation: Operation<RQ, RS>) {
        this.operation = operation
    }

    @OptMainBuilderDsl
    infix fun mainOperation(operation: RQ.() -> RS) {
        mainOperation(Operation { operation() })
    }

    @OptOperatorBuilderDsl
    infix fun after(operator: Operator<RQ, RS>) {
        this.operators.add(operator)
    }

    @OptOperatorBuilderDsl
    infix fun after(operator: RQ.(RS) -> Unit) {
        after(Operator { response -> operator(response) })
    }

    @OptRoleBuilderDsl
    infix fun install(role: OperationRole<RQ, Unit>) {
        roles.add(role)
    }

    @OptRoleBuilderDsl
    infix fun install(role: RQ.() -> Unit) {
        install(OperationRole { role() })
    }

    fun build(): Operation<RQ, RS> =
        DefaultOperation(
            operation = operation,
            roles = roles,
            operators = operators
        )
}

internal class DefaultOperation<RQ, RS>(
    private val operation: Operation<RQ, RS>,
    private val roles: MutableList<OperationRole<RQ, Unit>> = mutableListOf(),
    private val operators: MutableList<Operator<RQ, RS>> = mutableListOf(),
) : Operation<RQ, RS> {

    private fun RQ.before() =
        roles.forEach { it.operateRole(this) }

    private fun RQ.process() =
        operation.runOperation(this)

    private fun RQ.after(response: RS) =
        operators.forEach {
            it.runOperator(this, response)
        }

    override fun RQ.operate(): RS {
        before()
        return process().apply {
            after(this)
        }
    }
}
