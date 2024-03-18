package com.game.gameofthree.service

import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.Move
import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.domain.repository.MoveRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MoveService(
    private val moveRepository: MoveRepository
) {

    @Transactional
    fun addMove(player: Player, currentResult: Int, value: Int, game: Game): Move {
        return moveRepository.save(Move(player = player, value = value, currentResult = currentResult, game = game))
    }

    @Transactional
    fun addFirstMove(player: Player, initialValue: Int, game: Game): Move {
        return moveRepository.save(Move(player = player, value = null, currentResult = initialValue, game = game))
    }

    fun getLastTenMoves(gameId: String): List<Move> {
        return moveRepository.getLastThreeMoves(gameId)
    }
}
