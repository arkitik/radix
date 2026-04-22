package io.arkitik.radix.protocol.exposed

import org.jetbrains.exposed.v1.core.DatabaseConfig

/**
 * A functional interface for customizing Exposed framework database configuration.
 *
 * This interface defines a contract for applying custom configuration to an Exposed
 * [DatabaseConfig] builder. It enables flexible, composable database configuration
 * through a single customization point.
 *
 * Implementations can be provided as lambda expressions due to the functional interface
 * nature, making it convenient to compose multiple customizations.
 *
 * @see DatabaseConfig
 * @see DatabaseConfig.Builder
 *
 * @author Ibrahim Al-Tamimi 
 * @since 15:46, Wednesday, 22/04/2026
 **/
fun interface ExposedDatabaseConfigCustomizer {
    fun customize(builder: DatabaseConfig.Builder)
}