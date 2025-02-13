package io.arkitik.radix.starter.exposed

import io.arkitik.radix.starter.exposed.function.RadixSpringTransactionManager
import org.jetbrains.exposed.spring.autoconfigure.ExposedAutoConfiguration
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

/**
 * Created By Ibrahim Al-Tamimi 
 * Created At 1:15 PM, 30/06/2024
 */
@Configuration
@AutoConfiguration(after = [DataSourceAutoConfiguration::class])
@ImportAutoConfiguration(
    value = [ExposedAutoConfiguration::class],
    exclude = [
        DataSourceTransactionManagerAutoConfiguration::class,
        ExposedAutoConfiguration::class
    ]
)
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

    @Bean
    @Primary
    fun springTransactionManager(
        database: Database,
        databaseConfig: DatabaseConfig,
        @Value("\${spring.exposed.show-sql:false}")
        showSql: Boolean = false,
    ): RadixSpringTransactionManager {
        return RadixSpringTransactionManager(database, databaseConfig, showSql)
    }
}
