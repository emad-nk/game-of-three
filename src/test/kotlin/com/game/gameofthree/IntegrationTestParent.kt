package com.game.gameofthree

import io.restassured.RestAssured
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

/**
 * Base class for all Spring Boot integration tests.
 */
@ActiveProfiles("integration-test")
@ContextConfiguration(initializers = [Initializer::class])
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(PER_CLASS)
abstract class IntegrationTestParent {

    @LocalServerPort
    var localPort: Int = 0

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @Autowired
    private lateinit var redisTemplate: RedisTemplate<Any, Any>

    @BeforeAll
    @AfterEach
    fun setup() {
        resetDatabase()
        resetRedis()
        setupRestAssured()
    }

    private fun resetDatabase() {
        // We don't add a new table often, so hardcoding the list of tables here is not a big problem.
        // Having the list of tables helps a lot with ordering (tables with foreign keys must be deleted first).
        ALL_TABLES.forEach { table -> jdbcTemplate.update("DELETE FROM \"$table\"") }
    }

    private fun resetRedis() {
        redisTemplate.execute { conn -> conn.serverCommands().flushAll() }
    }

    private fun setupRestAssured() {
        RestAssured.port = localPort
    }

    companion object {
        private val ALL_TABLES = listOf(
            "move",
            "game",
            "player",
        )
    }
}

class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        System.setProperty("aws.accessKeyId", "id")
        System.setProperty("aws.secretAccessKey", "key")
        System.setProperty("aws.secretKey", "key")
    }
}
