package com.game.gameofthree.liveupdate

import com.game.gameofthree.controller.response.GameDTO

data class LiveUpdate(
    val channelName: String,
    val eventName: String,
    val gameDTO: GameDTO
)
