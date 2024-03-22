package com.game.gameofthree.service

import com.game.gameofthree.configuration.CacheNames.PLAYER_BY_USERNAME
import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.domain.repository.PlayerRepository
import com.game.gameofthree.exception.DuplicatePlayerException
import com.game.gameofthree.exception.EntityNotFoundException
import org.springframework.cache.annotation.Cacheable
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
) {

    fun createPlayer(username: String): Player {
        try {
            return playerRepository.save(Player(username = username))
        } catch (ex: DataIntegrityViolationException) {
            throw DuplicatePlayerException("A player with the same username $username exists")
        }
    }

    @Cacheable(value = [PLAYER_BY_USERNAME], key = "{#username}")
    fun findPlayer(username: String): Player {
        return playerRepository.findPlayerByUsername(username)
            ?: throw EntityNotFoundException("Player $username not found")
    }
}
