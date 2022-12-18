package io.arkitik.radix.develop.shared.error

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 29, **Thu Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
@Deprecated(
    message = "To be removed in v2.0.0, replaced by RadixError to avoid conflicts with kotlin.Error",
    replaceWith = ReplaceWith(
        expression = "RadixError",
        imports = ["io.arkitik.radix.develop.shared.error.RadixError"]
    )
)
data class Error(
    override val code: String?,
    override val message: String?,
) : ErrorResponse


interface ErrorResponse {
    val code: String?
    val message: String?
}
