package com.game.gameofthree.service

import com.game.gameofthree.domain.repository.PlayerRepository
import com.game.gameofthree.exception.DuplicatePlayerException
import com.game.gameofthree.exception.EntityNotFoundException
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DataIntegrityViolationException

class PlayerServiceTest {

    private val playerRepository = mockk<PlayerRepository>()
    private val playerService = PlayerService(playerRepository)

    @Test
    fun `throws not found exception when user does not exist by username`() {
        every { playerRepository.findPlayerByUsername(any()) } returns null

        assertThrows<EntityNotFoundException> { playerService.findPlayer("non-existent") }
    }

    @Test
    fun `throws DuplicatePlayerException when username exists and a new player wants to create the same username `() {
        every { playerRepository.save(any()) } throws DataIntegrityViolationException("duplicate")

        assertThrows<DuplicatePlayerException> { playerService.createPlayer("king") }
    }
}
