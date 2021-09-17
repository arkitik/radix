package io.arkitik.radix.starter.logger

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 12, **Fri February, 2021**
 * Project *radix* [https://arkitik.io]
 */
@RestController
class SampleController {
    @PostMapping("/post")
    fun data(@RequestBody map: SampleRequest): ResponseEntity<SampleRequest> {
        return ResponseEntity.ok(map)
    }

    @GetMapping("/get")
    fun dataGet(): ResponseEntity<Map<*, *>> {
        return ResponseEntity.ok(mapOf(Pair("Key", "Value")))
    }
}

data class SampleRequest(
    val userUuid: String,
    val title: String,
    val duration: String,
    val privacyType: String,
)