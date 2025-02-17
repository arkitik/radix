package io.arkitik.radix.starter.exposed

import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:15 PM, 30/06/2024
 */
@Configuration
@AutoConfiguration(after = [DataSourceAutoConfiguration::class])
@ImportAutoConfiguration(ExposedAutoConfiguration::class)
@EnableTransactionManagement
class RadixExposedStarter {
    @Bean
    @ConditionalOnMissingBean
    fun databaseConfig() =
        DatabaseConfig {
            useNestedTransactions = true
        }

    @Bean
    fun database(
        datasource: DataSource,
        databaseConfig: DatabaseConfig,
    ): Database {
        return Database.connect(
            datasource = datasource,
            databaseConfig = databaseConfig
        )
    }
}
