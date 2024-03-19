package com.game.gameofthree.service

import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.domain.repository.PlayerRepository
import com.game.gameofthree.exception.DuplicatePlayerException
import com.game.gameofthree.exception.EntityNotFoundException
import jakarta.validation.ConstraintViolationException
import org.springframework.stereotype.Service

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
) {

    fun createPlayer(username: String): Player {
        try {
            return playerRepository.save(Player(username = username))
        } catch (ex: ConstraintViolationException){
            throw DuplicatePlayerException("A player with the same username $username exists")
        }
    }

    fun findPlayer(username: String): Player {
        return playerRepository.findPlayerByUsername(username)
            ?: throw EntityNotFoundException("Player $username not found")
    }
}
