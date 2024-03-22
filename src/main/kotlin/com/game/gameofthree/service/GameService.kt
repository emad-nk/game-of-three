package com.game.gameofthree.service

import com.game.gameofthree.common.findBestDivisibleBy3
import com.game.gameofthree.common.getARandomNumberBetweenTwoAndThousand
import com.game.gameofthree.common.validateIfUserIsAllowedToMove
import com.game.gameofthree.common.validateInitialValue
import com.game.gameofthree.common.validateValue
import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.GameStatus.FINISHED
import com.game.gameofthree.domain.model.GameStatus.PLAYING
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.domain.model.Move
import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.domain.model.toDTO
import com.game.gameofthree.domain.repository.GameRepository
import com.game.gameofthree.exception.EntityNotFoundException
import com.game.gameofthree.liveupdate.GameEvent
import com.game.gameofthree.liveupdate.GameEvent.PLAYER_JOINED
import com.game.gameofthree.liveupdate.GameEvent.PLAYER_MOVED
import com.game.gameofthree.liveupdate.LiveUpdateService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class])
class GameService(
    private val gameRepository: GameRepository,
    private val moveService: MoveService,
    private val playerService: PlayerService,
    private val liveUpdateService: LiveUpdateService,
) {

    fun start(username: String): GameDTO {
        val player = findPlayer(username)
        val existingGame = gameRepository.findOldestGameWithStatusWaiting()
        if (existingGame != null) {
            val gameDTO = gameRepository.save(existingGame.copy(playerTwo = player, status = PLAYING)).toDTO()
            triggerGameUpdate(gameDTO, PLAYER_JOINED)
            return gameDTO
        }
        val newGame = Game(playerOne = player, status = WAITING)
        return gameRepository.save(newGame).toDTO()
    }

    fun manualMove(username: String, gameId: String, value: Int): GameDTO {
        val lastMove = moveService.getLastMove(gameId)
        return move(username = username, gameId = gameId, value = value, lastMove = lastMove)
    }

    fun automaticMove(username: String, gameId: String): GameDTO {
        val lastMove = moveService.getLastMove(gameId)
        if (lastMove != null) {
            val bestValue = findBestDivisibleBy3(number = lastMove.currentResult)
            return move(username = username, gameId = gameId, value = bestValue, lastMove = lastMove)
        }
        val randomValue = getARandomNumberBetweenTwoAndThousand()
        return move(username = username, gameId = gameId, value = randomValue, lastMove = null)
    }

    fun getGamesByStatus(status: String, pageable: Pageable): Page<GameDTO> {
        return gameRepository.findGamesByStatus(status = status.uppercase(), pageable).map { it.toDTO() }
    }

    fun getGame(gameId: String): GameDTO {
        val game = gameRepository.findByIdOrNull(id = gameId) ?: throw EntityNotFoundException("Game with id $gameId not found")
        val lastMove = moveService.getLastMove(gameId)
        return game.toDTO(lastMove = lastMove)
    }

    private fun move(username: String, gameId: String, value: Int, lastMove: Move?): GameDTO {
        val game = findAPlayingGame(gameId)
        val player = findPlayer(username)
        return if (lastMove != null) {
            handleNextMove(lastMove, player, value, game)
        } else {
            handleFirstMove(value, player, game)
        }.also {
            triggerGameUpdate(it, PLAYER_MOVED)
        }
    }

    private fun triggerGameUpdate(gameDTO: GameDTO, gameEvent: GameEvent) {
        liveUpdateService.triggerGameUpdate(gameDTO = gameDTO, gameEvent = gameEvent)
    }

    private fun handleFirstMove(value: Int, player: Player, game: Game): GameDTO {
        value.validateInitialValue()
        val move = moveService.addFirstMove(player, value, game)
        return game.toDTO(lastMove = move)
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
        return gameRepository.save(game.copy(winner = player, status = FINISHED))
            .toDTO(lastMove = move)
    }

    private fun findAPlayingGame(gameId: String): Game {
        return gameRepository.findAPlayingGame(gameId)
            ?: throw EntityNotFoundException("Game with id $gameId not found or the game is not in PLAYING state anymore")
    }

    private fun findPlayer(username: String): Player {
        return playerService.findPlayer(username)
    }

    companion object {
        const val WINNER_VALUE = 1
    }
}
