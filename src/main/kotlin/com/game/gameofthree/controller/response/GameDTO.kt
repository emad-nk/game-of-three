package com.game.gameofthree.controller.response

data class GameDTO(
    val id: String,
    val playerOne: PlayerDTO,
    val playerTwo: PlayerDTO? = null,
    val recentMoves: List<MoveDTO>
)
