package com.game.gameofthree.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("thread-pool")
data class ThreadPoolProperties(
    val size: Map<String, Int>,
)
