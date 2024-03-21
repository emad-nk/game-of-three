package com.game.gameofthree.controller

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.controller.ControllerExceptionHandler.ValidationError
import com.game.gameofthree.controller.request.PlayerRequestDTO
import com.game.gameofthree.controller.response.PlayerDTO
import com.game.gameofthree.service.PlayerService
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import org.apache.http.HttpStatus.SC_BAD_REQUEST
import org.apache.http.HttpStatus.SC_CREATED
import org.apache.http.HttpStatus.SC_NOT_FOUND
import org.apache.http.HttpStatus.SC_OK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PlayerControllerIT(
    @Autowired private val playerService: PlayerService,
) : IntegrationTestParent() {

    @Test
    fun `creates a player`() {
        val playerRequestDTO = PlayerRequestDTO(username = "kong")

        val response = given()
            .contentType(JSON)
            .body(playerRequestDTO)
            .`when`()
            .post(BASE_URL)
            .then()
            .log().ifValidationFails()
            .statusCode(SC_CREATED)
            .extract()
            .body()
            .`as`(PlayerDTO::class.java)

        assertThat(response.username).isEqualTo("kong")
    }

    @Test
    fun `throws a bad request exception when creating a duplicate user`() {
        playerService.createPlayer("kong")
        val playerRequestDTO = PlayerRequestDTO(username = "kong")

        val response = given()
            .contentType(JSON)
            .body(playerRequestDTO)
            .`when`()
            .post(BASE_URL)
            .then()
            .log().ifValidationFails()
            .statusCode(SC_BAD_REQUEST)
            .extract()
            .body()
            .`as`(ValidationError::class.java)

        assertThat(response.message).isEqualTo("A player with the same username kong exists")
    }

    @Test
    fun `gets a player`() {
        playerService.createPlayer("king")

        val response = given()
            .contentType(JSON)
            .`when`()
            .get("$BASE_URL/king")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_OK)
            .extract()
            .body()
            .`as`(PlayerDTO::class.java)

        assertThat(response.username).isEqualTo("king")
    }

    @Test
    fun `throws not found exception when getting non-existent player`() {
        val response = given()
            .contentType(JSON)
            .`when`()
            .get("$BASE_URL/king")
            .then()
            .log().ifValidationFails()
            .statusCode(SC_NOT_FOUND)
            .extract()
            .body()
            .`as`(ValidationError::class.java)

        assertThat(response.message).isEqualTo("Player king not found")
    }

    companion object {
        private const val BASE_URL = "/api/v1/players"
    }
}
