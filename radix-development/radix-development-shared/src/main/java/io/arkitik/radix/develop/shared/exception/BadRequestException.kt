package io.arkitik.radix.develop.shared.exception

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
class BadRequestException(
    val errors: List<ErrorResponse>,
) : RuntimeException() {
    override fun toString(): String {
        return errors.toString()
    }
}