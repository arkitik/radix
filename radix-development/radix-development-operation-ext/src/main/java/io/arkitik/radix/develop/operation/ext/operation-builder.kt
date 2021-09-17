package io.arkitik.radix.develop.operation.ext

import io.arkitik.radix.develop.operation.Operation
import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.operation.Operator

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 07  1:06 AM, **Fri July, 2021**
 * Project *radix* [https://arkitik.io]
 */

fun <RQ, RS> operationBuilder(
    builder: OperationBuilder<RQ, RS>.() -> Unit,
): Operation<RQ, RS> =
    OperationBuilder<RQ, RS>()
        .apply(builder)
        .build()

class OperationBuilder<RQ, RS> {
    private val roles: MutableList<OperationRole<RQ, Unit>> = mutableListOf()
    private val operators: MutableList<Operator<RQ, RS>> = mutableListOf()
    private lateinit var operation: Operation<RQ, RS>

    infix fun mainOperation(operation: Operation<RQ, RS>) {
        this.operation = operation
    }

    infix fun mainOperation(operation: RQ.() -> RS) {
        this.operation = object : Operation<RQ, RS> {
            override fun RQ.operate() = operation()
        }
    }

    infix fun after(operator: Operator<RQ, RS>) {
        this.operators.add(operator)
    }

    infix fun after(operator: RQ.(RS) -> Unit) {
        this.operators.add(object : Operator<RQ, RS> {
            override fun RQ.operate(response: RS) {
                operator(response)
            }
        })
    }

    infix fun install(role: OperationRole<RQ, Unit>) {
        roles.add(role)
    }

    infix fun install(role: RQ.() -> Unit) {
        roles.add(object : OperationRole<RQ, Unit> {
            override fun RQ.operateRole() {
                role()
            }
        })
    }

    fun build() =
        DefaultOperation(
            operation = operation,
            roles = roles,
            operators = operators
        )
}

class DefaultOperation<RQ, RS>(
    private val operation: Operation<RQ, RS>,
    private val roles: MutableList<OperationRole<RQ, Unit>> = mutableListOf(),
    private val operators: MutableList<Operator<RQ, RS>> = mutableListOf(),
) : Operation<RQ, RS> {

    private fun RQ.before() =
        roles.forEach { it.run { operateRole() } }

    private fun RQ.process() =
        operation.run { operate() }

    private fun RQ.after(response: RS) =
        operators.forEach {
            it.run { operate(response) }
        }

    override fun RQ.operate(): RS {
        before()
        return process().apply {
            after(this)
        }
    }
}
