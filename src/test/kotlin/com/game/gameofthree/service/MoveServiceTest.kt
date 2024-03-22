package com.game.gameofthree.service

import com.game.gameofthree.domain.repository.MoveRepository
import com.game.gameofthree.dummyGame
import com.game.gameofthree.dummyMove
import com.game.gameofthree.dummyPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest

class MoveServiceTest {

    private val moveRepository = mockk<MoveRepository>(relaxed = true)
    private val moveService = MoveService(moveRepository = moveRepository)

    @Test
    fun `calls the repository to save when adding the 1st move`() {
        val player = dummyPlayer()
        val game = dummyGame()
        val move = dummyMove(game = game, player = player)
        every { moveRepository.save(any()) } returns move

        moveService.addFirstMove(player = player, initialValue = 56, game = game)

        verify(exactly = 1) { moveRepository.save(any()) }
    }

    @Test
    fun `calls the repository to save when adding the 2nd move`() {
        val player = dummyPlayer()
        val game = dummyGame()
        val move = dummyMove(game = game, player = player)
        every { moveRepository.save(any()) } returns move

        moveService.addMove(player = player, currentResult = 56, value = 1, game = game)

        verify(exactly = 1) { moveRepository.save(any()) }
    }

    @Test
    fun `calls the repository to get all moves paginated`() {
        moveService.getAllMoves(gameId = "game-id", pageable = PageRequest.of(0, 1))

        verify(exactly = 1) { moveRepository.getAllMoves(any(), any()) }
    }
}
