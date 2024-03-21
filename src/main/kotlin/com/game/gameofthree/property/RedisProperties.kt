package com.game.gameofthree.property

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties("redis")
data class RedisProperties(var ttl: Map<String, Duration>)
