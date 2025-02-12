package io.arkitik.radix.develop.shared.ext

import io.arkitik.radix.develop.shared.error.ErrorResponse
import io.arkitik.radix.develop.shared.exception.BadRequestException
import io.arkitik.radix.develop.shared.exception.ForbiddenException
import io.arkitik.radix.develop.shared.exception.InternalException
import io.arkitik.radix.develop.shared.exception.NotAcceptableException
import io.arkitik.radix.develop.shared.exception.NotAuthorizedException
import io.arkitik.radix.develop.shared.exception.ResourceNotFoundException
import io.arkitik.radix.develop.shared.exception.UnprocessableEntityException

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
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

fun ErrorResponse.forbidden() = ForbiddenException(this)
