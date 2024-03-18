package com.game.gameofthree.domain.model

import com.game.gameofthree.controller.response.MoveDTO
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.Instant
import java.time.Instant.now
import java.util.UUID

@Entity
data class Move(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val value: Int?,
    val currentResult: Int,
    @ManyToOne
    val player: Player,
    @ManyToOne
    val game: Game,
    val timestamp: Instant = now(),
)

fun Move.toDTO(): MoveDTO {
    return MoveDTO(
        id = id,
        value = value,
        currentResult = currentResult,
        player = player.toDTO(),
        gameId = game.id,
        timestamp = timestamp,
    )
}
