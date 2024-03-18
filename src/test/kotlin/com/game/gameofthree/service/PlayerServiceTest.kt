package com.game.gameofthree.service

import com.game.gameofthree.domain.repository.PlayerRepository
import com.game.gameofthree.exception.UserNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PlayerServiceTest {

    private val playerRepository = mockk<PlayerRepository>()
    private val playerService = PlayerService(playerRepository)

    @Test
    fun `throws not found exception when user does not exist by username`() {
        every { playerRepository.findPlayerByUsername(any()) } returns null

        assertThrows<UserNotFoundException> { playerService.findPlayer("non-existent") }
    }
}
