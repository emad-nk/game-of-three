package com.game.gameofthree.controller

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.common.validateIfUserIsAllowedToMove
import com.game.gameofthree.controller.ControllerExceptionHandler.ValidationError
import com.game.gameofthree.controller.request.GameRequestDTO
import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.controller.validation.MoveRequestValidator
import com.game.gameofthree.controller.validation.MoveRequestValidator.Companion
import com.game.gameofthree.controller.validation.MoveRequestValidator.Companion.MOVE_REQUEST_VALIDATION_ERROR
import com.game.gameofthree.domain.model.GameStatus.PLAYING
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.domain.model.toDTO
import com.game.gameofthree.domain.repository.GameRepository
import com.game.gameofthree.dummyGame
import com.game.gameofthree.dummyGameDTO
import com.game.gameofthree.dummyMoveDTO
import com.game.gameofthree.dummyMoveRequestDTO
import com.game.gameofthree.dummyPlayer
import com.game.gameofthree.service.GameService
import com.game.gameofthree.service.MoveService
import com.game.gameofthree.service.PlayerService
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import org.apache.http.HttpStatus
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_NOT_FOUND
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class GameControllerIT(
    @Autowired private val gameRepository: GameRepository,
    @Autowired private val gameService: GameService,
    @Autowired private val moveService: MoveService,
    @Autowired private val playerService: PlayerService,
) : IntegrationTestParent() {

    @Test
    fun `player starts a game`() {
        val player = playerService.createPlayer("king")
        val gameRequestDTO = GameRequestDTO(username = "king")
        val expectedGameDTO = dummyGameDTO(
            playerOne = player.toDTO(),
            status = WAITING,
            lastMove = null,
            winner = null,
        )

        val response = given()
            .contentType(JSON)
            .body(gameRequestDTO)
            .`when`()
            .post("$BASE_URL/games/starts")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_CREATED)
            .extract()
            .body()
            .`as`(GameDTO::class.java)

        assertThat(response)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedGameDTO)
    }

    @Test
    fun `player makes an automatic move`() {
        val player1 = playerService.createPlayer("king")
        val player2 = playerService.createPlayer("kong")
        val game = gameRepository.save(dummyGame(playerOne = player1, playerTwo = player2, status = PLAYING))
        moveService.addFirstMove(player = player1, initialValue = 56, game = game)
        val expectedGameDTO = dummyGameDTO(
            playerOne = player1.toDTO(),
            playerTwo = player2.toDTO(),
            status = PLAYING,
            lastMove = dummyMoveDTO(player = player2.toDTO(), value = 1, currentResult = 19, gameId = game.id),
            winner = null,
        )
        val moveRequestDTO = dummyMoveRequestDTO(
            username = player2.username,
            gameId = game.id,
            value = null,
        )

        val response = given()
            .contentType(JSON)
            .body(moveRequestDTO)
            .`when`()
            .post("$BASE_URL/games/automatic-moves")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_CREATED)
            .extract()
            .body()
            .`as`(GameDTO::class.java)

        assertThat(response)
            .usingRecursiveComparison()
            .ignoringFields("id", "lastMove.id", "lastMove.timestamp")
            .isEqualTo(expectedGameDTO)
    }

    @Test
    fun `throws not found exception when player starts a game and username does not exist`() {
        val gameRequestDTO = GameRequestDTO(username = "king")

        val response = given()
            .contentType(JSON)
            .body(gameRequestDTO)
            .`when`()
            .post("$BASE_URL/games/starts")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_NOT_FOUND)
            .extract()
            .body()
            .`as`(ValidationError::class.java)

        assertThat(response.message).isEqualTo("Player king not found")
    }

    @Test
    fun `throws bad request exception when player starts with an invalid value`() {
        val player = playerService.createPlayer(username = "king")
        val game = gameRepository.save(dummyGame(playerOne = player, status = PLAYING))
        val moveRequestDTO = dummyMoveRequestDTO(
            username = player.username,
            gameId = game.id,
            value = 0,
        )

        val response = given()
            .contentType(JSON)
            .body(moveRequestDTO)
            .`when`()
            .post("$BASE_URL/games/manual-moves")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_BAD_REQUEST)
            .extract()
            .body()
            .`as`(ValidationError::class.java)

        assertThat(response.message).isEqualTo("Initial value should be greater than 2 and less than 1000")
    }

    @Test
    fun `throws bad request exception when player plays again`() {
        val player = playerService.createPlayer(username = "king")
        val game = gameRepository.save(dummyGame(playerOne = player, status = PLAYING))
        moveService.addFirstMove(player = player, initialValue = 56, game = game)
        val moveRequestDTO = dummyMoveRequestDTO(
            username = player.username,
            gameId = game.id,
            value = 0,
        )

        val response = given()
            .contentType(JSON)
            .body(moveRequestDTO)
            .`when`()
            .post("$BASE_URL/games/manual-moves")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_BAD_REQUEST)
            .extract()
            .body()
            .`as`(ValidationError::class.java)

        assertThat(response.message).isEqualTo("Player king has already played, other player should play now")
    }

    @Test
    fun `throws bad request exception when value is null in a manual move`() {
        val moveRequestDTO = dummyMoveRequestDTO(
            username = "king",
            gameId = "some-game",
            value = null,
        )

        val response = given()
            .contentType(JSON)
            .body(moveRequestDTO)
            .`when`()
            .post("$BASE_URL/games/manual-moves")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_BAD_REQUEST)
            .extract()
            .body()
            .`as`(ValidationError::class.java)

        assertThat(response.message).isEqualTo(MOVE_REQUEST_VALIDATION_ERROR)
    }

    companion object {
        private const val BASE_URL = "/api/v1"
    }
}
