package com.game.gameofthree.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "pusher")
data class PusherProperties(
    val appId: String,
    val key: String,
    val secret: String,
    val cluster: String,
    val channelPrefixes: List<String> = emptyList(),
)
