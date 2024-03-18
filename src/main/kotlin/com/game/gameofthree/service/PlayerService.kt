package com.game.gameofthree.service

import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.domain.repository.PlayerRepository
import com.game.gameofthree.exception.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
) {

    fun findPlayer(username: String): Player {
        return playerRepository.findPlayerByUsername(username)
            ?: throw EntityNotFoundException("Player $username not found")
    }
}
