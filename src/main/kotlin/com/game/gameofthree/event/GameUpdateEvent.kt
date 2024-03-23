package com.game.gameofthree.event

import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.liveupdate.GameEvent

data class GameUpdateEvent(
    val gameDTO: GameDTO,
    val gameEvent: GameEvent,
)
