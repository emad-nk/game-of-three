package com.game.gameofthree.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.util.UUID.randomUUID

@Entity
data class Player(
    @Id
    val id: String = randomUUID().toString(),
    val username: String
)
