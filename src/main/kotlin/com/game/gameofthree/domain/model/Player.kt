package com.game.gameofthree.domain.model

import com.game.gameofthree.controller.response.PlayerDTO
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.io.Serializable
import java.util.UUID.randomUUID

@Entity
data class Player(
    @Id
    val id: String = randomUUID().toString(),
    val username: String,
): Serializable {
    companion object {
        private const val serialVersionUID = 6498378134993474269L
    }
}

fun Player.toDTO(): PlayerDTO {
    return PlayerDTO(
        id = id,
        username = username,
    )
}
