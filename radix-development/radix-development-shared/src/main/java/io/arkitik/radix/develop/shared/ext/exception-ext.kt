package io.arkitik.radix.develop.shared.ext

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 10, **Wed February, 2021**
 * Project *radix* [https://arkitik.io]
 */

infix fun <R> R?.notAcceptable(error: ErrorResponse) =
    this ?: throw error.notAcceptable()

infix fun <R> R?.unprocessableEntity(error: ErrorResponse) =
    this ?: throw error.unprocessableEntity()

infix fun <R> R?.notAuthorized(error: ErrorResponse) =
    this ?: throw error.notAuthorized()

infix fun <R> R?.resourceNotFound(error: ErrorResponse) =
    this ?: throw error.notFound()

infix fun <R> R?.internalError(error: ErrorResponse) =
    this ?: throw error.internal()

infix fun <R> R?.forbidden(error: ErrorResponse) =
    this ?: throw error.forbidden()

infix fun <R> R?.badRequest(error: ErrorResponse) =
    this ?: throw error.badRequest()

infix fun <R> R?.badRequest(errors: List<ErrorResponse>) =
    this ?: throw errors.badRequest()
