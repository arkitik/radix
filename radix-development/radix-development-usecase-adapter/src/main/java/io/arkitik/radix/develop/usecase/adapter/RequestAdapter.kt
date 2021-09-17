package io.arkitik.radix.develop.usecase.adapter

import io.arkitik.radix.develop.usecase.model.UseCaseRequest

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 07, **Sat Nov, 2020**
 * Project **radix** [https://arkitik.io](https://arkitik.io)
 */
data class RequestAdapter<R>(
    val request: R,
) : UseCaseRequest