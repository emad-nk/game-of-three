package com.game.gameofthree

import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.liveupdate.UpdateDTO
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
