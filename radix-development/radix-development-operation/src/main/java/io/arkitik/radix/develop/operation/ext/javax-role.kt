package io.arkitik.radix.develop.operation.ext

import io.arkitik.radix.develop.operation.OperationRole
import io.arkitik.radix.develop.shared.error.ErrorResponse
import io.arkitik.radix.develop.shared.error.RadixError
import io.arkitik.radix.develop.shared.ext.badRequest
import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
object DefaultJavaXValidator : JavaXValidator<Any>()

typealias ErrorMapper = (ConstraintViolation<*>) -> ErrorResponse

open class JavaXValidator<T>(
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator,
    errorMapper: ErrorMapper? = null,
) : OperationRole<T, Unit> {
    private val errorMapper = errorMapper ?: { constraint ->
        RadixError(constraint.message, constraint.propertyPath.toString())
    }

    final override fun T.operateRole() {
        validator.run {
            validate(this@operateRole)
        }.takeIf {
            it.isNotEmpty()
        }?.map(errorMapper)
            ?.badRequest()?.run {
                throw this
            }
    }
}

fun <RQ, RS> OperationBuilder<RQ, RS>.installJavaXValidation(
    validator: Validator = Validation.buildDefaultValidatorFactory().validator,
    errorMapper: ErrorMapper? = null,
) {
    install(JavaXValidator(validator, errorMapper))
}
