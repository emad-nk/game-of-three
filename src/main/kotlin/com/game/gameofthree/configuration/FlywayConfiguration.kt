package com.game.gameofthree.configuration

import org.flywaydb.core.api.configuration.FluentConfiguration
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer
import org.springframework.context.annotation.Configuration

@Configuration
class FlywayConfiguration : FlywayConfigurationCustomizer {

    override fun customize(configuration: FluentConfiguration) {
        // Disable the transactional lock in Flyway that breaks all non-transactional migrations since v9.1.2 of the plugin
        // See https://github.com/flyway/flyway/issues/3508
        configuration.configuration(
            mapOf(
                "flyway.postgresql.transactional.lock" to "false",
            ),
        )
    }
}
