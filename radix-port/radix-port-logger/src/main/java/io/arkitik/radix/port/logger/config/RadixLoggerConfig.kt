package io.arkitik.radix.port.logger.config

import org.slf4j.event.Level
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.bind.DefaultValue

/**
 * Created By [*Ibrahim AlTamimi *](https://www.linkedin.com/in/iloom/)
 * Created At 30, **Fri Oct, 2020**
 * Project *radix* [https://arkitik.io]
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "arkitik.radix.logger")
data class RadixLoggerConfig(
    @DefaultValue("DEFAULT") val formatter: FormatterType,
    @DefaultValue("########") val mask: String,
    @Deprecated("Use ignored.fields instead")
    val maskedKeys: Set<String> = HashSet(),
    val loggerName: Class<*> = RadixLoggerConfig::class.java,
    @DefaultValue("DEBUG") val level: Level,
    val ignored: IgnoredConfig = IgnoredConfig(fields = maskedKeys),
) {
    @ConstructorBinding
    data class IgnoredConfig(
        val urls: Set<String> = HashSet(),
        val fields: Set<String> = HashSet(),
    )
}