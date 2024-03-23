package com.game.gameofthree.service

import com.game.gameofthree.domain.repository.GameRepository
import com.game.gameofthree.dummyGame
import com.game.gameofthree.dummyMove
import com.game.gameofthree.dummyPlayer
import com.game.gameofthree.event.EventPublisher
import com.game.gameofthree.liveupdate.GameEvent.PLAYER_JOINED
import com.game.gameofthree.liveupdate.GameEvent.PLAYER_MOVED
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test

class GameServiceTest {

    private val gameRepository = mockk<GameRepository>()
    private val moveService = mockk<MoveService>()
    private val playerService = mockk<PlayerService>()
    private val eventPublisher = mockk<EventPublisher>(relaxed = true)
    private val gameService = GameService(
        gameRepository = gameRepository,
        moveService = moveService,
        playerService = playerService,
        eventPublisher = eventPublisher,
    )

    @Test
    fun `sends player update when a player joins the game`() {
        every { gameRepository.findOldestGameWithStatusWaiting() } returns dummyGame()
        every { gameRepository.save(any()) } returns dummyGame()
        every { playerService.findPlayer(any()) } returns dummyPlayer()

        gameService.start(username = "king")

        verify(exactly = 1) { eventPublisher.publishUpdateEvent(any(), PLAYER_JOINED) }
    }

    @Test
    fun `sends player update when a player make a move`() {
        val player = dummyPlayer()
        val game = dummyGame()
        every { moveService.getLastMove(any()) } returns null
        every { gameRepository.findAPlayingGame(any()) } returns game
        every { playerService.findPlayer(any()) } returns player
        every {
            moveService.addFirstMove(any(), any(), any())
        } returns dummyMove(player = player, game = game)

        gameService.manualMove(username = "king", gameId = game.id, value = 56)

        verify(exactly = 1) { eventPublisher.publishUpdateEvent(any(), PLAYER_MOVED) }
    }
}
