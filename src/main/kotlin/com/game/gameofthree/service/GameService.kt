package com.game.gameofthree.service

import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.domain.model.toDTO
import com.game.gameofthree.domain.repository.GameRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val moveService: MoveService,
    private val playerService: PlayerService,
) {

    @Transactional
    fun start(username: String): GameDTO {
        val player = playerService.findPlayer(username = username)
        val existingGame = gameRepository.findOldestGameWithStatusWaiting()
        existingGame?.let {
            return existingGame.copy(playerTwo = player).toDTO()
        }
        val game = gameRepository.save(Game(playerOne = player, status = WAITING))
        return game.toDTO()
    }


}
