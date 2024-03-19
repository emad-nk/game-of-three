package com.game.gameofthree.liveupdate

import com.game.gameofthree.dummyGameDTO
import com.pusher.rest.Pusher
import com.pusher.rest.data.Result
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class LiveUpdateServiceTest {

    private val pusher: Pusher = mockk {
        every { trigger(any<String>(), any<String>(), any()) } returns Result.fromHttpCode(200, "")
    }

    @Test
    fun `sends message to prefixed channel`() {
        val liveUpdateService = LiveUpdateService(pusher, listOf("prefix"))
        val gameDTO = dummyGameDTO()

        liveUpdateService.triggerGameUpdate(LiveUpdate("my-channel", "my-event", gameDTO))

        verify(exactly = 1) { pusher.trigger("prefix-my-channel", "my-event", gameDTO) }
    }

    @Test
    fun `does not send message to channel without prefix`() {
        val liveUpdateService = LiveUpdateService(pusher, emptyList())

        liveUpdateService.triggerGameUpdate(LiveUpdate("my-channel", "my-event", dummyGameDTO()))

        verify(exactly = 0) { pusher.trigger(any<String>(), any<String>(), any()) }
    }
}
