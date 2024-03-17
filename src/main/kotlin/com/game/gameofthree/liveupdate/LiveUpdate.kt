package com.game.gameofthree.liveupdate

data class LiveUpdate(
    val channelName: String,
    val eventName: String,
    val updateDTO: UpdateDTO
)
