package com.game.gameofthree.domain.model

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
    val value: Int,
    @ManyToOne
    val player: Player,
    @ManyToOne
    val game: Game,
    val timestamp: Instant = now(),
)
