package io.arkitik.radix.develop.shared.ext

import io.arkitik.radix.develop.shared.error.ErrorResponse
import io.arkitik.radix.develop.shared.exception.*

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
fun ErrorResponse.notFound() = ResourceNotFoundException(this)

fun ErrorResponse.notAuthorized() = NotAuthorizedException(this)

fun ErrorResponse.notAcceptable() = NotAcceptableException(this)

fun ErrorResponse.unprocessableEntity() = UnprocessableEntityException(this)

fun ErrorResponse.internal() = InternalException(this)

fun ErrorResponse.badRequest() = listOf(this).badRequest()

fun List<ErrorResponse>.badRequest() = BadRequestException(this)