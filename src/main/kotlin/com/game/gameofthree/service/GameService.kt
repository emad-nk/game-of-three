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
    fun start(playerUsername: String, initialValue: Int): GameDTO {
        val player = playerService.findPlayer(username = playerUsername)
        val game = gameRepository.save(Game(playerOne = player, status = WAITING))
        val move = listOf(moveService.addFirstMove(player = player, game = game, initialValue = initialValue))
        return game.toDTO(recentMoves = move)
    }


}
