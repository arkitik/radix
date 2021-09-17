package io.arkitik.radix.develop.usecase.validation.func

import io.arkitik.radix.develop.shared.error.Error
import io.arkitik.radix.develop.shared.error.ErrorResponse
import io.arkitik.radix.develop.shared.ext.badRequest
import io.arkitik.radix.develop.usecase.model.UseCaseRequest
import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator as JavaXValidator

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
open class DefaultUseCaseValidator internal constructor(
    private val validator: JavaXValidator = Validation.buildDefaultValidatorFactory().validator,
    private val errorMapper: ErrorMapper = DefaultErrorMapper(),
) : UseCaseValidator {
    override fun <RQ : UseCaseRequest> RQ.validate() {
        validator.run {
            validate(this@validate)
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

    companion object {
        fun create(
            validator: JavaXValidator = Validation.buildDefaultValidatorFactory().validator,
            errorMapper: ErrorMapper = DefaultErrorMapper(),
        ): UseCaseValidator {
            return DefaultUseCaseValidator(
                validator, errorMapper
            )
        }
    }

    class DefaultErrorMapper : ErrorMapper {
        override fun <RQ : UseCaseRequest> ConstraintViolation<RQ>.mapToError(): ErrorResponse {
            val nodes = propertyPath.toList()
            return when {
                nodes.isNotEmpty() -> {
                    Error(nodes.last().name, message)
                }
                else -> Error(message, propertyPath.toString())
            }
        }
    }
}