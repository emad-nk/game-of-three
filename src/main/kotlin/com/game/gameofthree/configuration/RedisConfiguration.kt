package com.game.gameofthree.configuration

import com.game.gameofthree.property.RedisProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import kotlin.reflect.full.declaredMemberProperties

@Configuration
@EnableConfigurationProperties(RedisProperties::class)
@EnableCaching
class RedisConfiguration(val properties: RedisProperties) {

    @Bean("cacheManager")
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager =
        RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(defaultCacheConfiguration())
            .withInitialCacheConfigurations(buildCacheConfigurations())
            .build()

    private fun buildCacheConfigurations() = CacheNames.all()
        .associateWith { defaultCacheConfiguration().withEntryTtl(it) }
        .toMutableMap()

    private fun defaultCacheConfiguration() = defaultCacheConfig().disableCachingNullValues()

    private fun RedisCacheConfiguration.withEntryTtl(cacheName: String) = this.entryTtl(properties.ttl.getValue(cacheName))
}

object CacheNames {
    const val PLAYING_GAME_BY_ID = "playing-game-by-id"
    const val PLAYER_BY_USERNAME = "player-by-user-name"

    fun all(): List<String> {
        return this::class.declaredMemberProperties.map { it.getter.call() as String }
    }
}
