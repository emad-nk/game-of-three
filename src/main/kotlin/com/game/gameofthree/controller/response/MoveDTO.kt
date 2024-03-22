package com.game.gameofthree.controller.response

import java.time.Instant

data class MoveDTO(
    val id: String,
    val value: Int?,
    val currentResult: Int,
    val player: PlayerDTO,
    val gameId: String,
    val timestamp: Instant,
)
