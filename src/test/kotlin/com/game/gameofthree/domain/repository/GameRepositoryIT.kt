package com.game.gameofthree.domain.repository

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.dummyGame
import com.game.gameofthree.dummyPlayer
import java.time.Instant.now
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class GameRepositoryIT(
    @Autowired private val playerRepository: PlayerRepository,
    @Autowired private val gameRepository: GameRepository,
) : IntegrationTestParent() {

    @Test
    fun `finds the oldest game that already has a player WAITING for an opponent`() {
        val player = dummyPlayer(username = "king")
        val game1 = dummyGame(playerOne = player, status = WAITING, createdAt = now().minusSeconds(50))
        val game2 = dummyGame(playerOne = player, status = WAITING, createdAt = now())

        playerRepository.save(player)
        gameRepository.saveAll(listOf(game1, game2))

        assertThat(gameRepository.findOldestGameWithStatusWaiting()).isEqualTo(game1)
    }

    @Test
    fun `returns null when there is no game with status WAITING`() {
        assertThat(gameRepository.findOldestGameWithStatusWaiting()).isNull()
    }
}
