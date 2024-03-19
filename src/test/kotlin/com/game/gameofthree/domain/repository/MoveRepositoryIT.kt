package com.game.gameofthree.domain.repository

import com.game.gameofthree.IntegrationTestParent
import com.game.gameofthree.dummyGame
import com.game.gameofthree.dummyMove
import com.game.gameofthree.dummyPlayer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.DESC

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
        moveRepository.save(dummyMove(player = player, game = game1))
        val move = moveRepository.save(dummyMove(player = player, game = game1))
        moveRepository.save(dummyMove(player = player, game = game2))

        assertThat(moveRepository.findAll()).hasSize(5)

        val lastTMove = moveRepository.getLastMove(gameId = game1.id)

        assertThat(lastTMove).isNotNull
        assertThat(lastTMove).isEqualTo(move)
    }

    @Test
    fun `gets all the moves paginated`() {
        val player = dummyPlayer(username = "king")
        val game1 = dummyGame(playerOne = player)
        val game2 = dummyGame(playerOne = player)

        playerRepository.save(player)
        gameRepository.saveAll(listOf(game1, game2))

        moveRepository.save(dummyMove(player = player, game = game1))
        moveRepository.save(dummyMove(player = player, game = game1))
        val move1 = moveRepository.save(dummyMove(player = player, game = game1))
        val move2 = moveRepository.save(dummyMove(player = player, game = game1))
        moveRepository.save(dummyMove(player = player, game = game2))

        assertThat(moveRepository.findAll()).hasSize(5)

        val lastMoves = moveRepository
            .getAllMoves(
                gameId = game1.id,
                pageable = PageRequest.of(0, 2, Sort.by(DESC, "timestamp")),
            )

        assertThat(lastMoves).hasSize(2)
        assertThat(lastMoves).containsExactly(move2, move1)
    }

    @Test
    fun `returns null when player does not exist by username`() {
        assertThat(playerRepository.findPlayerByUsername("king")).isNull()
    }
}
