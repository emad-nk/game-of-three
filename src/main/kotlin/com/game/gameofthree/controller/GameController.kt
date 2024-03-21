package com.game.gameofthree.controller

import com.game.gameofthree.controller.request.GameRequestDTO
import com.game.gameofthree.controller.request.MoveRequestDTO
import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.controller.response.MoveDTO
import com.game.gameofthree.service.GameService
import com.game.gameofthree.service.MoveService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
@RequestMapping("/api/v1/")
class GameController(
    private val gameService: GameService,
    private val moveService: MoveService,
) {

    @Operation(description = "Starts a new game or joins a player to a game which the other player is waiting")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created"),
        ],
    )
    @PostMapping("games")
    @ResponseStatus(code = CREATED)
    fun startAGame(@RequestBody gameRequestDTO: GameRequestDTO): GameDTO {
        return gameService.start(gameRequestDTO.username)
    }

    @PostMapping("games/manual-moves")
    @Operation(description = "Makes a manual move")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created"),
        ],
    )
    @ResponseStatus(code = CREATED)
    fun makeAManualMove(@Valid @RequestBody moveRequestDTO: MoveRequestDTO): GameDTO {
        return gameService.manualMove(
            username = moveRequestDTO.username,
            gameId = moveRequestDTO.gameId,
            value = moveRequestDTO.value!!,
        )
    }

    @PostMapping("games/automatic-moves")
    @Operation(description = "Makes an automatic move")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "Created"),
        ],
    )
    @ResponseStatus(code = CREATED)
    fun makeAnAutomaticMove(@RequestBody moveRequestDTO: MoveRequestDTO): GameDTO {
        return gameService.automaticMove(
            username = moveRequestDTO.username,
            gameId = moveRequestDTO.gameId,
        )
    }

    @GetMapping("games/{gameId}")
    @Operation(description = "Gets a game")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
        ],
    )
    @ResponseStatus(code = OK)
    fun getGame(
        @PathVariable gameId: String,
    ): GameDTO {
        return gameService.getGame(gameId = gameId)
    }

    @GetMapping("moves/{gameId}")
    @Operation(description = "Gets the history of moves related to a specific game")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Successful"),
        ],
    )
    @ResponseStatus(code = OK)
    fun getMoves(
        @PathVariable gameId: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(value = "sort-by", defaultValue = "timestamp") sortBy: String,
        @RequestParam(value = "sort-order", defaultValue = "desc") sortOrder: String,
    ): Page<MoveDTO> {
        val direction = if (sortOrder.equals("asc", ignoreCase = true)) Direction.ASC else Direction.DESC
        val pageable = PageRequest.of(page, size, Sort.by(direction, sortBy))
        return moveService.getAllMoves(
            gameId = gameId,
            pageable = pageable,
        )
    }
}
