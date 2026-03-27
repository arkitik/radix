package io.arkitik.radix.starter.logger.rest.template

import org.slf4j.LoggerFactory
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.restclient.RestTemplateBuilder
import org.springframework.boot.runApplication
import org.springframework.web.client.getForObject

/**
 * @author Ibrahim Al-Tamimi 
 * @since 10:20, Thursday, 26/03/2026
 **/
@SpringBootApplication
class SampleRestClientApp(
    private val restTemplateBuilder: RestTemplateBuilder,
) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(SampleRestClientApp::class.java)
    override fun run(vararg args: String) {
        val restTemplate = restTemplateBuilder.build()
        val apiResponse = restTemplate.getForObject<Any>("https://api.restful-api.dev/objects/1")
        logger.info("Response: $apiResponse")
    }
}

fun main(args: Array<String>) {
    runApplication<SampleRestClientApp>(*args) {
        setBannerMode(Banner.Mode.OFF)
        setLogStartupInfo(false)
    }
}