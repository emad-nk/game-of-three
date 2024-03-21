package com.game.gameofthree

import com.game.gameofthree.controller.request.MoveRequestDTO
import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.controller.response.MoveDTO
import com.game.gameofthree.controller.response.PlayerDTO
import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.GameStatus
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.domain.model.Move
import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.domain.model.toDTO
import java.time.Instant
import java.time.Instant.now
import java.util.UUID.randomUUID

fun dummyGameDTO(
    id: String = randomUUID().toString(),
    playerOne: PlayerDTO = dummyPlayer().toDTO(),
    playerTwo: PlayerDTO? = null,
    status: GameStatus = WAITING,
    lastMove: MoveDTO? = dummyMoveDTO(),
    winner: PlayerDTO? = null
): GameDTO =
    GameDTO(
        id = id,
        playerOne = playerOne,
        playerTwo = playerTwo,
        status = status,
        lastMove = lastMove,
        winner = winner
    )

fun dummyMoveDTO(
    id: String = randomUUID().toString(),
    value: Int? = null,
    currentResult: Int = 56,
    player: PlayerDTO = dummyPlayer().toDTO(),
    gameId: String = randomUUID().toString(),
    timestamp: Instant = now()
): MoveDTO =
    MoveDTO(
        id = id,
        value = value,
        currentResult = currentResult,
        player = player,
        gameId = gameId,
        timestamp = timestamp
    )

fun dummyMoveRequestDTO(
    username: String,
    gameId: String,
    value: Int? = null,
): MoveRequestDTO =
    MoveRequestDTO(
        username= username,
        gameId = gameId,
        value = value
    )

fun dummyPlayer(
    id: String = randomUUID().toString(),
    username: String = "king",
): Player =
    Player(
        id = id,
        username = username,
    )

fun dummyGame(
    id: String = randomUUID().toString(),
    playerOne: Player = dummyPlayer(username = "king"),
    playerTwo: Player? = null,
    status: GameStatus = WAITING,
    winner: Player? = null,
    createdAt: Instant = now()
): Game =
    Game(
        id = id,
        playerOne = playerOne,
        playerTwo = playerTwo,
        status = status,
        winner = winner,
        createdAt = createdAt,
    )

fun dummyMove(
    id: String = randomUUID().toString(),
    value: Int? = null,
    currentResult: Int = 56,
    player: Player,
    game: Game,
    timestamp: Instant = now(),
): Move =
    Move(
        id = id,
        value = value,
        currentResult = currentResult,
        player = player,
        game = game,
        timestamp = timestamp,
    )
