package com.game.gameofthree

import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.GameStatus
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.domain.model.Move
import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.liveupdate.UpdateDTO
import java.time.Instant
import java.time.Instant.now
import java.util.UUID.randomUUID

fun dummyUpdateDTO(
    userId: String = randomUUID().toString(),
    added: Int = 1,
    resultingNumber: Int = 12,
): UpdateDTO =
    UpdateDTO(
        userId = userId,
        added = added,
        resultingNumber = resultingNumber,
    )

fun dummyPlayer(
    id: String = randomUUID().toString(),
    username: String,
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
    winnerId: String? = null,
    createdAt: Instant = now()
): Game =
    Game(
        id = id,
        playerOne = playerOne,
        playerTwo = playerTwo,
        status = status,
        winnerUsername = winnerId,
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
