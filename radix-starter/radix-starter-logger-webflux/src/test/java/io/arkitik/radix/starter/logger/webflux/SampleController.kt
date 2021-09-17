package io.arkitik.radix.starter.logger.webflux

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 12, **Fri February, 2021**
 * Project *radix* [https://arkitik.io]
 */
@RestController
class SampleController {
    @PostMapping("/post")
    fun data(@RequestBody map: SampleRequest): Flux<SampleRequest> {
        return Flux.just(map)
    }

    @PostMapping("/post1")
    fun datas(@RequestBody map: SampleRequest): Mono<SampleRequest> {
        return Mono.just(map)
    }

    @GetMapping("/get")
    fun dataGet(): Mono<Map<*, *>> {
        return Mono.just(mapOf(Pair("sample", "sample data")))
    }
}

data class SampleRequest(
    val userUuid: String,
    val title: String,
    val duration: String,
    val privacyType: String,
)