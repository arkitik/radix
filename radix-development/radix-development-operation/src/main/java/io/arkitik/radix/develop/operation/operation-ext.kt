package io.arkitik.radix.develop.operation

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 02, **Tue March, 2021**
 * Project *radix* [https://arkitik.io]
 */
@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "operate()",
        "io.arkitik.radix.develop.operation.ext.runOperation"
    ),
)
infix fun <RQ, RS> Operation<RQ, RS>.runOperation(request: RQ) = request.operate()

@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "operateRole(RQ)",
        "io.arkitik.radix.develop.operation.ext.operateRole"
    ),
)
infix fun <RQ, RS> OperationRole<RQ, RS>.operateRole(request: RQ) = request.operateRole()

@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "runOperator(RQ,RS)",
        "io.arkitik.radix.develop.operation.ext.runOperator"
    ),
)
fun <RQ, RS> Operator<RQ, RS>.runOperator(request: RQ, response: RS) = request.operate(response)

@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "FunctionOperation {}",
        "io.arkitik.radix.develop.operation.ext.FunctionOperation"
    ),
)
class FunctionOperation<RQ, RS>(
    private val function: RQ.() -> RS,
) : Operation<RQ, RS> {
    override fun RQ.operate() = function()
}

@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "MapperOperation {}",
        "io.arkitik.radix.develop.operation.ext.MapperOperation"
    ),
)
class MapperOperation<RQ, RS>(
    private val mapper: RQ.() -> RS,
) : Operation<RQ, RS> {
    override fun RQ.operate() = mapper()
}
