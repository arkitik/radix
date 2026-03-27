package io.arkitik.radix.starter.logger.rest.template

import org.springframework.boot.restclient.RestTemplateCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zalando.logbook.Logbook
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor

/**
 * @author Ibrahim Al-Tamimi 
 * @since 10:20, Thursday, 26/03/2026
 **/
@Configuration
class RestTemplateRadixLoggerAutoConfiguration {
    @Bean
    fun restTemplateRadixLoggerCustomizer(logbook: Logbook): RestTemplateCustomizer =
        RestTemplateCustomizer {
            it.interceptors.add(LogbookClientHttpRequestInterceptor(logbook))
        }
}