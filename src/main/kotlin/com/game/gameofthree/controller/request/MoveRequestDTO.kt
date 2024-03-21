package com.game.gameofthree.controller.request

data class MoveRequestDTO(
    val username: String,
    val gameId: String,
    val value: Int?

)
