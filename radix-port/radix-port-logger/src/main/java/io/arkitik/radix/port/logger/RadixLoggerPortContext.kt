package io.arkitik.radix.port.logger

import io.arkitik.radix.port.logger.config.RadixLoggerConfig
import io.arkitik.radix.port.logger.function.RadixHttpLogWriter
import io.arkitik.radix.port.logger.function.RadixLoggerCustomizer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.AntPathMatcher
import org.zalando.logbook.Logbook
import org.zalando.logbook.core.BodyFilters
import org.zalando.logbook.core.Conditions
import org.zalando.logbook.core.DefaultSink
import org.zalando.logbook.core.HeaderFilters
import org.zalando.logbook.core.QueryFilters
import org.zalando.logbook.json.JsonBodyFilters
import java.util.*

/**
 * Created By [*Ibrahim Al-Tamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
@Configuration
@EnableConfigurationProperties(value = [RadixLoggerConfig::class])
class RadixLoggerPortContext {
    @Bean
    @ConditionalOnMissingBean
    fun logbook(
        radixLoggerConfig: RadixLoggerConfig,
        customizers: List<RadixLoggerCustomizer>,
    ): Logbook =
        with(Logbook.builder()) {
            val pathMatcher = AntPathMatcher()
            correlationId { UUID.randomUUID().toString() }
            bodyFilter(
                BodyFilters.replaceFormUrlEncodedProperty(
                    radixLoggerConfig.ignored.fields,
                    radixLoggerConfig.mask
                )
            )
            bodyFilter(
                JsonBodyFilters.replaceJsonStringProperty(
                    radixLoggerConfig.ignored.fields,
                    radixLoggerConfig.mask
                )
            )
            bodyFilter(
                JsonBodyFilters.replaceJsonStringProperty(
                    { key ->
                        radixLoggerConfig.ignored.fields.any {
                            pathMatcher.match(it, key)
                        }
                    },
                    radixLoggerConfig.mask

                )
            )
            headerFilter(HeaderFilters.replaceHeaders(radixLoggerConfig.ignored.fields, radixLoggerConfig.mask))
            headerFilter(
                HeaderFilters.replaceHeaders(
                    { key ->
                        radixLoggerConfig.ignored.fields.any {
                            pathMatcher.match(it, key)
                        }
                    },
                    radixLoggerConfig.mask
                )
            )
            headerFilter(
                HeaderFilters.replaceCookies(
                    { radixLoggerConfig.ignored.fields.contains(it) },
                    radixLoggerConfig.mask
                )
            )
            queryFilter(
                QueryFilters.replaceQuery(
                    { radixLoggerConfig.ignored.fields.contains(it) },
                    radixLoggerConfig.mask
                )
            )
            condition(
                Conditions.exclude(
                    radixLoggerConfig.ignored.urls
                        .map {
                            Conditions.requestTo(it)
                        }
                )
            )
            sink(
                DefaultSink(
                    radixLoggerConfig.formatter.formatter,
                    RadixHttpLogWriter(
                        LoggerFactory.getLogger(
                            radixLoggerConfig.loggerName
                        ),
                        radixLoggerConfig.level
                    )
                )
            )
        }.also { builder ->
            customizers.forEach { customizer ->
                customizer.customize(builder)
            }
        }.build()
}
