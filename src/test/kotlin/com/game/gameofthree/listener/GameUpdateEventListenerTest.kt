package com.game.gameofthree.listener

import com.game.gameofthree.dummyGameDTO
import com.game.gameofthree.event.GameUpdateEvent
import com.game.gameofthree.liveupdate.GameEvent.PLAYER_MOVED
import com.game.gameofthree.liveupdate.LiveUpdateService
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class GameUpdateEventListenerTest {

    private val liveUpdateService = mockk<LiveUpdateService>(relaxed = true)
    private val gameUpdateEventListener = GameUpdateEventListener(
        liveUpdateService = liveUpdateService,
    )

    @Test
    fun `calls liveUpdateService after receiving event`() {
        val gameDTO = dummyGameDTO()
        gameUpdateEventListener.handleGameUpdate(gameUpdateEvent = GameUpdateEvent(gameDTO = gameDTO, gameEvent = PLAYER_MOVED))

        verify(exactly = 1) { liveUpdateService.triggerGameUpdate(gameDTO = gameDTO, gameEvent = PLAYER_MOVED) }
    }
}
