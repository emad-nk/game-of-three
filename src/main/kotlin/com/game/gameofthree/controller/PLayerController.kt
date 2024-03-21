package com.game.gameofthree.controller

import com.game.gameofthree.controller.request.PlayerRequestDTO
import com.game.gameofthree.controller.response.PlayerDTO
import com.game.gameofthree.domain.model.toDTO
import com.game.gameofthree.service.PlayerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/api/v1/players")
class PLayerController(
    private val playerService: PlayerService,
) {

    @Operation(description = "Creates a new player")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created"),
            ApiResponse(responseCode = "400", description = "Bad Request - user already exists"),
        ],
    )
    @PostMapping
    @ResponseStatus(code = CREATED)
    fun createPlayer(@RequestBody playerRequestDTO: PlayerRequestDTO): PlayerDTO {
        return playerService.createPlayer(username = playerRequestDTO.username).toDTO()
    }

    @Operation(description = "Gets a player")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
            ApiResponse(responseCode = "404", description = "Not found"),
        ],
    )
    @GetMapping("{username}")
    @ResponseStatus(code = OK)
    fun startAGame(@PathVariable username: String): PlayerDTO {
        return playerService.findPlayer(username = username).toDTO()
    }
}
