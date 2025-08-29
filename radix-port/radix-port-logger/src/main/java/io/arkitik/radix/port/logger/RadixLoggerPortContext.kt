package io.arkitik.radix.port.logger

import io.arkitik.radix.port.logger.config.RadixLoggerConfig
import io.arkitik.radix.port.logger.function.RadixHttpLogWriter
import io.arkitik.radix.port.logger.function.RadixLoggerCustomizer
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.zalando.logbook.BodyFilters
import org.zalando.logbook.Conditions
import org.zalando.logbook.DefaultSink
import org.zalando.logbook.HeaderFilters
import org.zalando.logbook.Logbook
import org.zalando.logbook.QueryFilters
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
            headerFilter(HeaderFilters.replaceHeaders(radixLoggerConfig.ignored.fields, radixLoggerConfig.mask))
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
