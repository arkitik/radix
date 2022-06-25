package io.arkitik.radix.develop.operation.function

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.shared.error.Error
import io.arkitik.radix.develop.shared.error.ErrorResponse
import io.arkitik.radix.develop.shared.ext.badRequest
import javax.validation.ConstraintViolation
import javax.validation.Validation

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "DefaultJavaXValidator",
        "io.arkitik.radix.develop.operation.ext.DefaultJavaXValidator"
    ),
)
object DefaultJavaXValidator : JavaXValidator()

@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "JavaXValidator",
        "io.arkitik.radix.develop.operation.ext.JavaXValidator"
    ),
)
open class JavaXValidator internal constructor(
    private val validator: javax.validation.Validator = Validation.buildDefaultValidatorFactory().validator,
    private val errorMapper: ErrorMapper = DefaultErrorMapper,
) : OperationRole<Any, Unit> {
    override fun Any.operateRole() {
        validator.run {
            validate(this@operateRole)
        }.takeIf {
            it.isNotEmpty()
        }?.map {
            errorMapper.run {
                it.mapToError()
            }
        }?.badRequest()?.run {
            throw this
        }
    }
}

@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "DefaultErrorMapper",
        "io.arkitik.radix.develop.operation.ext.DefaultErrorMapper"
    ),
)
object DefaultErrorMapper : ErrorMapper {
    override fun <RQ> ConstraintViolation<RQ>.mapToError(): ErrorResponse {
        val nodes = propertyPath.toList()
        return when {
            nodes.isNotEmpty() -> {
                Error(nodes[nodes.size - 1].name, message)
            }

            else -> Error(message, propertyPath.toString())
        }
    }
}

@Deprecated(
    message = "To be removed in v2.0.0, replaced by module radix-development-operation-ext",
    replaceWith = ReplaceWith(
        expression = "ErrorMapper",
        "io.arkitik.radix.develop.operation.ext.ErrorMapper"
    ),
)
interface ErrorMapper {
    fun <RQ> ConstraintViolation<RQ>.mapToError(): ErrorResponse
}
