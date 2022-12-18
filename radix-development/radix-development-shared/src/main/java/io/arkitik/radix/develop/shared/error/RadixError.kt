package io.arkitik.radix.develop.shared.error

data class RadixError(
    override val code: String?,
    override val message: String?,
) : ErrorResponse
