package com.game.gameofthree.listener

import com.game.gameofthree.configuration.SpringAsyncConfig.Companion.GAME_UPDATE_EXECUTOR
import com.game.gameofthree.event.GameUpdateEvent
import com.game.gameofthree.liveupdate.LiveUpdateService
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class GameUpdateEventListener(
    private val liveUpdateService: LiveUpdateService,
) {

    @TransactionalEventListener
    @Async(GAME_UPDATE_EXECUTOR)
    fun handleGameUpdate(gameUpdateEvent: GameUpdateEvent) {
        liveUpdateService.triggerGameUpdate(gameDTO = gameUpdateEvent.gameDTO, gameEvent = gameUpdateEvent.gameEvent)
    }
}
