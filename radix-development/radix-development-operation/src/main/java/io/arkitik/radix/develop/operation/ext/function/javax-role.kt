package io.arkitik.radix.develop.operation.ext.function

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.shared.error.ErrorResponse
import io.arkitik.radix.develop.shared.error.RadixError
import io.arkitik.radix.develop.shared.ext.badRequest
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validation
import jakarta.validation.Validator

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
object DefaultJakartaValidator : JakartaValidator<Any>()

open class JakartaValidator<T>(
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator,
    private val errorMapper: ErrorMapper = DefaultErrorMapper,
) : OperationRole<T, Unit> {
    final override fun T.operateRole() {
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

object DefaultErrorMapper : ErrorMapper {
    override fun <RQ> ConstraintViolation<RQ>.mapToError(): ErrorResponse {
        val nodes = propertyPath.toList()
        return when {
            nodes.isNotEmpty() -> {
                RadixError(nodes[nodes.size - 1].name, message)
            }

            else -> RadixError(message, propertyPath.toString())
        }
    }
}

interface ErrorMapper {
    fun <RQ> ConstraintViolation<RQ>.mapToError(): ErrorResponse
}
