package io.arkitik.radix.develop.shared.exception

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
abstract class BaseException(
    val error: ErrorResponse,
) : RuntimeException() {
    override val message: String
        get() = error.toString()

    override fun toString() = error.toString()
}
