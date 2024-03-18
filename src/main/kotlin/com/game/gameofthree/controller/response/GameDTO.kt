package com.game.gameofthree.controller.response

import com.game.gameofthree.domain.model.GameStatus

data class GameDTO(
    val id: String,
    val playerOne: PlayerDTO,
    val playerTwo: PlayerDTO?,
    val status: GameStatus,
    val lastMove: MoveDTO?,
    val winnerUsername: String?
)
