package com.game.gameofthree.domain.repository

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.dummyGame
import com.game.gameofthree.dummyMove
import com.game.gameofthree.dummyPlayer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class MoveRepositoryIT(
    @Autowired private val moveRepository: MoveRepository,
    @Autowired private val playerRepository: PlayerRepository,
    @Autowired private val gameRepository: GameRepository,
) : IntegrationTestParent() {

    @Test
    fun `finds the last 3 moves by the gameId sorted by latest first`() {
        val player = dummyPlayer(username = "king")
        val game1 = dummyGame(playerOne = player)
        val game2 = dummyGame(playerOne = player)

        playerRepository.save(player)
        gameRepository.saveAll(listOf(game1, game2))

        moveRepository.save(dummyMove(player = player, game = game1))
        val move1 = moveRepository.save(dummyMove(player = player, game = game1))
        val move2 = moveRepository.save(dummyMove(player = player, game = game1))
        val move3 = moveRepository.save(dummyMove(player = player, game = game1))
        moveRepository.save(dummyMove(player = player, game = game2))

        assertThat(moveRepository.findAll()).hasSize(5)

        val lastThreeMoves = moveRepository.getLastThreeMoves(gameId = game1.id)

        assertThat(lastThreeMoves).hasSize(3)
        assertThat(lastThreeMoves).containsExactly(move3, move2, move1)
    }

    @Test
    fun `returns null when player does not exist by username`() {
        assertThat(playerRepository.findPlayerByUsername("king")).isNull()
    }
}
