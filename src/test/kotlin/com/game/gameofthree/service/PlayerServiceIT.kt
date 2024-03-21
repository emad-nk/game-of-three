package com.game.gameofthree.service

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.configuration.CacheNames
import com.game.gameofthree.configuration.CacheNames.PLAYER_BY_USERNAME
import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.GameStatus.FINISHED
import com.game.gameofthree.domain.model.GameStatus.PLAYING
import com.game.gameofthree.domain.model.GameStatus.WAITING
import com.game.gameofthree.domain.model.toDTO
import com.game.gameofthree.domain.repository.GameRepository
import com.game.gameofthree.dummyGame
import com.game.gameofthree.dummyGameDTO
import com.game.gameofthree.dummyMoveDTO
import com.game.gameofthree.exception.EntityNotFoundException
import com.game.gameofthree.exception.WrongPlayerException
import com.game.gameofthree.exception.WrongValueException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate

class PlayerServiceIT(
    @Autowired private val playerService: PlayerService,
    @Autowired private val redisTemplate: RedisTemplate<String, String>
) : IntegrationTestParent() {

    @Test
    fun `caches player when getting a player by id`() {
        playerService.createPlayer("king")

        assertThat(redisTemplate.keys("$PLAYER_BY_USERNAME*")).isEmpty()

        val player = playerService.findPlayer("king")

        assertThat(player.username).isEqualTo("king")
        assertThat(redisTemplate.keys("$PLAYER_BY_USERNAME*")).hasSize(1)
    }

}
