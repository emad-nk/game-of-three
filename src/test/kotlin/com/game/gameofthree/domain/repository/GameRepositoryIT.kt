package com.game.gameofthree.domain.repository

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.configuration.CacheNames.PLAYING_GAME_BY_ID
import com.game.gameofthree.domain.model.GameStatus.FINISHED
import com.game.gameofthree.domain.model.GameStatus.PLAYING
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.dummyGame
import com.game.gameofthree.dummyPlayer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import java.time.Instant.now

class GameRepositoryIT(
    @Autowired private val playerRepository: PlayerRepository,
    @Autowired private val gameRepository: GameRepository,
    @Autowired private val redisTemplate: RedisTemplate<String, String>,
) : IntegrationTestParent() {

    @Test
    fun `finds the oldest game that already has a player WAITING for an opponent`() {
        val player = playerRepository.save(dummyPlayer(username = "king"))
        val game1 = dummyGame(playerOne = player, status = WAITING, createdAt = now().minusSeconds(50))
        val game2 = dummyGame(playerOne = player, status = WAITING, createdAt = now())

        gameRepository.saveAll(listOf(game1, game2))

        assertThat(gameRepository.findOldestGameWithStatusWaiting()).isEqualTo(game1)
    }

    @Test
    fun `finds a playing game and caches it and evicts it when the game gets updated`() {
        val player = playerRepository.save(dummyPlayer(username = "king"))
        val game1 = dummyGame(playerOne = player, status = PLAYING)
        val game2 = dummyGame(playerOne = player, status = PLAYING)

        gameRepository.saveAll(listOf(game1, game2))

        assertThat(redisTemplate.keys("$PLAYING_GAME_BY_ID*")).isEmpty()

        val game = gameRepository.findAPlayingGame(game1.id)

        assertThat(game).isEqualTo(game1)
        assertThat(redisTemplate.keys("$PLAYING_GAME_BY_ID*")).hasSize(1)

        gameRepository.save(game1.copy(status = FINISHED))
        assertThat(redisTemplate.keys("$PLAYING_GAME_BY_ID*")).isEmpty()
    }

    @Test
    fun `returns null when there is no game with status WAITING`() {
        assertThat(gameRepository.findOldestGameWithStatusWaiting()).isNull()
    }
}
