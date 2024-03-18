package com.game.gameofthree.controller.response

import java.time.Instant

data class MoveDTO(
    val id: String,
    val value: Int,
    val playerDTO: PlayerDTO,
    val gameId: String,
    val timestamp: Instant,
)
