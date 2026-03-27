package io.arkitik.radix.starter.logger.rest.template

import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

/**
 * @author Ibrahim Al-Tamimi 
 * @since 10:20, Thursday, 26/03/2026
 **/
@SpringBootApplication
class SampleRestClientApp(
    private val restClient: RestClient.Builder,
) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(SampleRestClientApp::class.java)
    override fun run(vararg args: String) {
        val restClient = this@SampleRestClientApp.restClient.build()
        val apiResponse = restClient
            .get()
            .uri("https://api.restful-api.dev/objects/1")
            .retrieve()
            .body<Any>()
        logger.info("Response: $apiResponse")
    }
}

fun main(args: Array<String>) {
    runApplication<SampleRestClientApp>(*args) {
        setBannerMode(Banner.Mode.OFF)
        setLogStartupInfo(false)
    }
}