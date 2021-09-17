package io.arkitik.radix.develop.shared.error

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
open class SharedException(
    private val errors: List<ErrorResponse> = ArrayList(),
) : RuntimeException() {
    override val message: String
        get() = errors.toString()

    open class Builder internal constructor() {
        private val errors: MutableList<ErrorResponse> = ArrayList()
        fun with(code: String, message: String): Builder {
            return with(Error(code, message))
        }

        fun with(error: ErrorResponse): Builder {
            errors.add(error)
            return this
        }

        fun with(error: () -> ErrorResponse): Builder {
            errors.add(error())
            return this
        }

        fun throwMeIfNotEmpty() {
            if (errors.isNotEmpty()) throw SharedException(errors)
        }

        @Throws(SharedException::class)
        fun throwMe() {
            throw SharedException(errors)
        }

        fun build(): SharedException {
            return SharedException(errors)
        }
    }

    companion object {
        fun builder(): Builder {
            return Builder()
        }
    }
}