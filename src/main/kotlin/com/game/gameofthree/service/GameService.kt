package com.game.gameofthree.service

import com.game.gameofthree.common.validateIfUserIsAllowedToMove
import com.game.gameofthree.common.validateInitialValue
import com.game.gameofthree.common.validateValue
import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.GameStatus
import com.game.gameofthree.domain.model.GameStatus.FINISHED
import com.game.gameofthree.domain.model.GameStatus.PLAYING
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.domain.model.Move
import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.domain.model.toDTO
import com.game.gameofthree.domain.repository.GameRepository
import com.game.gameofthree.exception.EntityNotFoundException
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
        val player = findPlayer(username)
        val existingGame = gameRepository.findOldestGameWithStatusWaiting()
        existingGame?.let {
            return gameRepository.save(existingGame.copy(playerTwo = player, status = PLAYING)).toDTO()
        }
        return gameRepository.save(Game(playerOne = player, status = WAITING)).toDTO()
    }

    @Transactional
    fun move(username: String, gameId: String, value: Int): GameDTO {
        val game = findAPlayingGame(gameId)
        val player = findPlayer(username)
        val lastMove = moveService.getLastMove(gameId = gameId)
        lastMove?.let {
            return handleNextMove(it, player, value, game)
        }
        return handleFirstMove(value, player, game)
    }

    private fun handleFirstMove(value: Int, player: Player, game: Game): GameDTO {
        value.validateInitialValue()
        val move = moveService.addFirstMove(player, value, game)
        return game.updateGameStatus(PLAYING).toDTO(lastMove = move)
    }

    private fun handleNextMove(lastMove: Move, player: Player, value: Int, game: Game): GameDTO {
        player.validateIfUserIsAllowedToMove(lastMove)
        value.validateValue()
        val currentResult = (lastMove.currentResult + value) / 3
        val move = moveService.addMove(player = player, currentResult = currentResult, value = value, game = game)
        if (currentResult == WINNER_VALUE) {
            return finishTheGame(game, player, move)
        }
        return game.toDTO(lastMove = move)
    }

    private fun finishTheGame(game: Game, player: Player, move: Move): GameDTO {
        return gameRepository.save(game.copy(winnerUsername = player.username, status = FINISHED))
            .toDTO(lastMove = move)
    }

    private fun findAPlayingGame(gameId: String): Game {
        return gameRepository.findAPlayingGame(gameId)
            ?: throw EntityNotFoundException("Game with id $gameId not found or the game is not in PLAYING state anymore")
    }

    private fun findPlayer(username: String): Player {
        return playerService.findPlayer(username)
    }

    private fun Game.updateGameStatus(gameStatus: GameStatus): Game {
        return gameRepository.save(this.copy(status = gameStatus))
    }

    companion object {
        const val WINNER_VALUE = 1
    }
}
