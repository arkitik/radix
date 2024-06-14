package io.arkitik.radix.develop.shared.exception

import io.arkitik.radix.develop.shared.error.ErrorResponse

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 5:06 PM, 14/06/2024
 */
class ForbiddenException(
    error: ErrorResponse,
) : BaseException(error)
