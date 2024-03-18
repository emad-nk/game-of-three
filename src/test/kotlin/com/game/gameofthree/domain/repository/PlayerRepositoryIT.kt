package com.game.gameofthree.domain.repository

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.dummyPlayer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PlayerRepositoryIT(
    @Autowired private val playerRepository: PlayerRepository,
) : IntegrationTestParent() {

    @Test
    fun `finds a player by its username`() {
        val player = dummyPlayer(username = "king")

        playerRepository.save(player)

        val foundPlayer = playerRepository.findPlayerByUsername("king")

        assertThat(foundPlayer).isNotNull
        assertThat(foundPlayer!!.id).isEqualTo(player.id)
    }

    @Test
    fun `returns null when player does not exist by username`() {
        assertThat(playerRepository.findPlayerByUsername("king")).isNull()
    }
}
