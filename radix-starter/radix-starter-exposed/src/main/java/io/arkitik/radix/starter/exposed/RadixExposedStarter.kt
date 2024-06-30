package io.arkitik.radix.starter.exposed

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:15 PM, 30/06/2024
 */
@Configuration
@ImportAutoConfiguration(
    value = [ExposedAutoConfiguration::class],
    exclude = [DataSourceTransactionManagerAutoConfiguration::class]
)
class RadixExposedStarter {
    @Bean
    @ConditionalOnMissingBean
    fun databaseConfig() =
        DatabaseConfig {
            useNestedTransactions = true
        }
}
