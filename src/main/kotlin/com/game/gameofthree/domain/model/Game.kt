package com.game.gameofthree.domain.model

import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.domain.model.GameStatus.WAITING
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.Instant
import java.time.Instant.now
import java.util.UUID.randomUUID

@Entity
data class Game(
    @Id
    val id: String = randomUUID().toString(),
    @ManyToOne
    val playerOne: Player,
    @ManyToOne
    val playerTwo: Player? = null,
    @Enumerated(STRING)
    val status: GameStatus = WAITING,
    val winnerId: String? = null,
    val createdAt: Instant = now()
)

fun Game.toDTO(recentMoves: List<Move> = emptyList()): GameDTO {
    return GameDTO(
        id = id,
        playerOne = playerOne.toDTO(),
        playerTwo = playerTwo?.toDTO(),
        recentMoves = recentMoves.map { it.toDTO() }
    )
}
