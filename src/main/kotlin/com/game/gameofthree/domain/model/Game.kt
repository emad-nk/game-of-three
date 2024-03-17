package com.game.gameofthree.domain.model

import com.game.gameofthree.domain.model.GameStatus.STARTED
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.util.UUID.randomUUID

@Entity
data class Game(
    @Id
    val id: String = randomUUID().toString(),
    @ManyToOne
    val playerOne: Player,
    @ManyToOne
    val playerTwo: Player,
    @Enumerated(STRING)
    val status: GameStatus = STARTED,
    val winnerId: String? = null,
)
