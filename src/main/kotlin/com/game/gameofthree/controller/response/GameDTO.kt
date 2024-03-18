package com.game.gameofthree.controller.response

data class GameDTO(
    val id: String,
    val playerOneDTO: PlayerDTO,
    val playerTwoDTO: PlayerDTO? = null,
    val lastTenMoves: List<MoveDTO>
)
