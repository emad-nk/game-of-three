package com.game.gameofthree.domain.repository

import com.game.gameofthree.domain.model.Player
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerRepository : JpaRepository<Player, String> {

    fun findPlayerByUsername(username: String): Player?
}
