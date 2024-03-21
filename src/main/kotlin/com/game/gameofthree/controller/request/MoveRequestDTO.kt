package com.game.gameofthree.controller.request

import com.game.gameofthree.controller.validation.ValidateMoveRequest

@ValidateMoveRequest
data class MoveRequestDTO(
    val username: String,
    val gameId: String,
    val value: Int?
)
