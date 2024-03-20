package com.game.gameofthree.service

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.GameStatus.FINISHED
import com.game.gameofthree.domain.model.GameStatus.PLAYING
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.domain.model.toDTO
import com.game.gameofthree.domain.repository.GameRepository
import com.game.gameofthree.dummyGameDTO
import com.game.gameofthree.dummyMoveDTO
import com.game.gameofthree.exception.WrongPlayerException
import com.game.gameofthree.exception.WrongValueException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired

class GameServiceIT(
    @Autowired private val gameService: GameService,
    @Autowired private val gameRepository: GameRepository,
    @Autowired private val playerService: PlayerService,
    @Autowired private val moveService: MoveService,
) : IntegrationTestParent() {

    @Nested
    inner class PlayingGameManualTests {
        @Test
        fun `starts the game when username exists`() {
            val player = playerService.createPlayer("king")
            val expectedGameDTO = dummyGameDTO(
                playerOne = player.toDTO(),
                playerTwo = null,
                status = WAITING,
                lastMove = null,
                winner = null,
            )

            val gameDTO = gameService.start(username = player.username)

            assertThat(gameDTO)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedGameDTO)
        }

        @Test
        fun `player joins an existing game when player starts and other opponent is waiting and has already started the game`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val expectedGameDTO1 = dummyGameDTO(
                playerOne = player1.toDTO(),
                playerTwo = null,
                status = WAITING,
                lastMove = null,
                winner = null,
            )
            val expectedGameDTO2 = expectedGameDTO1.copy(
                playerTwo = player2.toDTO(),
                status = PLAYING,
            )

            val gameDTO1 = gameService.start(username = player1.username)

            assertThat(gameDTO1)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedGameDTO1)

            val gameDTO2 = gameService.start(username = player2.username)

            assertThat(gameDTO2)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedGameDTO2)
        }

        @Test
        fun `player makes move`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            val expectedGameDTO = dummyGameDTO(
                playerOne = player1.toDTO(),
                playerTwo = player2.toDTO(),
                status = PLAYING,
                lastMove = dummyMoveDTO(value = null, currentResult = 56, player = player1.toDTO(), gameId = game.id),
                winner = null,
            )

            val gameDTO = gameService.manualMove(player1.username, gameId = game.id, value = 56)

            assertThat(gameDTO)
                .usingRecursiveComparison()
                .ignoringFields("id", "lastMove.id", "lastMove.timestamp")
                .isEqualTo(expectedGameDTO)
        }

        @Test
        fun `player cannot make initial move with value less than 3`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))

            assertThrows<WrongValueException> { gameService.manualMove(player1.username, gameId = game.id, value = 2) }
        }

        @Test
        fun `player makes second move which is only allowed with -1, 0 and 1`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            moveService.addFirstMove(player = player1, initialValue = 56, game = game)

            val expectedGameDTO = dummyGameDTO(
                playerOne = player1.toDTO(),
                playerTwo = player2.toDTO(),
                status = PLAYING,
                lastMove = dummyMoveDTO(value = 1, currentResult = 19, player = player2.toDTO(), gameId = game.id),
                winner = null,
            )

            val gameDTO = gameService.manualMove(player2.username, gameId = game.id, value = 1)

            assertThat(gameDTO)
                .usingRecursiveComparison()
                .ignoringFields("id", "lastMove.id", "lastMove.timestamp")
                .isEqualTo(expectedGameDTO)
        }

        @Test
        fun `player cannot make second move with values other than -1, 0 and 1`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            moveService.addFirstMove(player = player1, initialValue = 56, game = game)

            assertThrows<WrongValueException> { gameService.manualMove(player2.username, gameId = game.id, value = 5) }
        }

        @Test
        fun `same player cannot move twice`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            moveService.addFirstMove(player = player1, initialValue = 56, game = game)
            moveService.addMove(player = player2, value = 1, currentResult = 19, game = game)


            assertThrows<WrongPlayerException> { gameService.manualMove(player2.username, gameId = game.id, value = 1) }
        }

        @Test
        fun `finishes the game when a player get to 1`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            moveService.addFirstMove(player = player1, initialValue = 4, game = game)

            val expectedGameDTO = dummyGameDTO(
                playerOne = player1.toDTO(),
                playerTwo = player2.toDTO(),
                status = FINISHED,
                lastMove = dummyMoveDTO(value = -1, currentResult = 1, player = player2.toDTO(), gameId = game.id),
                winner = player2.toDTO(),
            )

            val gameDTO = gameService.manualMove(player2.username, gameId = game.id, value = -1)

            assertThat(gameDTO)
                .usingRecursiveComparison()
                .ignoringFields("id", "lastMove.id", "lastMove.timestamp")
                .isEqualTo(expectedGameDTO)
        }
    }

    @Nested
    inner class PlayingGameAutomaticTests {
        @Test
        fun `player makes move`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            val expectedGameDTO = dummyGameDTO(
                playerOne = player1.toDTO(),
                playerTwo = player2.toDTO(),
                status = PLAYING,
                lastMove = dummyMoveDTO(value = null, currentResult = 56, player = player1.toDTO(), gameId = game.id),
                winner = null,
            )

            val gameDTO = gameService.autoMove(player1.username, gameId = game.id)

            assertThat(gameDTO)
                .usingRecursiveComparison()
                .ignoringFields("id", "lastMove.id", "lastMove.timestamp", "lastMove.currentResult")
                .isEqualTo(expectedGameDTO)
        }

        @Test
        fun `player makes best second move automatically`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            moveService.addFirstMove(player = player1, initialValue = 56, game = game)

            val expectedGameDTO = dummyGameDTO(
                playerOne = player1.toDTO(),
                playerTwo = player2.toDTO(),
                status = PLAYING,
                lastMove = dummyMoveDTO(value = 1, currentResult = 19, player = player2.toDTO(), gameId = game.id),
                winner = null,
            )

            val gameDTO = gameService.autoMove(player2.username, gameId = game.id)

            assertThat(gameDTO)
                .usingRecursiveComparison()
                .ignoringFields("id", "lastMove.id", "lastMove.timestamp")
                .isEqualTo(expectedGameDTO)
        }

        @Test
        fun `same player cannot move twice`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            moveService.addFirstMove(player = player1, initialValue = 56, game = game)
            moveService.addMove(player = player2, value = 1, currentResult = 19, game = game)


            assertThrows<WrongPlayerException> { gameService.autoMove(player2.username, gameId = game.id) }
        }

        @Test
        fun `finishes the game when a player get to 1`() {
            val player1 = playerService.createPlayer("king")
            val player2 = playerService.createPlayer("kong")
            val game = gameRepository.save(Game(playerOne = player1, playerTwo = player2, status = PLAYING))
            moveService.addFirstMove(player = player1, initialValue = 4, game = game)

            val expectedGameDTO = dummyGameDTO(
                playerOne = player1.toDTO(),
                playerTwo = player2.toDTO(),
                status = FINISHED,
                lastMove = dummyMoveDTO(value = -1, currentResult = 1, player = player2.toDTO(), gameId = game.id),
                winner = player2.toDTO(),
            )

            val gameDTO = gameService.autoMove(player2.username, gameId = game.id)

            assertThat(gameDTO)
                .usingRecursiveComparison()
                .ignoringFields("id", "lastMove.id", "lastMove.timestamp")
                .isEqualTo(expectedGameDTO)
        }
    }
}
