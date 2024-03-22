package com.game.gameofthree.domain.model

import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.domain.model.GameStatus.WAITING
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.io.Serializable
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
    @ManyToOne
    val winner: Player? = null,
    val createdAt: Instant = now(),
) : Serializable {
    companion object {
        private const val serialVersionUID = 6498378134993474268L
    }
}

fun Game.toDTO(lastMove: Move? = null): GameDTO {
    return GameDTO(
        id = id,
        playerOne = playerOne.toDTO(),
        playerTwo = playerTwo?.toDTO(),
        status = status,
        winner = winner?.toDTO(),
        lastMove = lastMove?.toDTO(),
    )
}
