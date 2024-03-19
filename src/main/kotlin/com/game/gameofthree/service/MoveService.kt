package com.game.gameofthree.service

import com.game.gameofthree.controller.response.MoveDTO
import com.game.gameofthree.domain.model.Game
import com.game.gameofthree.domain.model.Move
import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.domain.model.toDTO
import com.game.gameofthree.domain.repository.MoveRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
import org.springframework.transaction.annotation.Transactional

@Service
class MoveService(
    private val moveRepository: MoveRepository
) {

    @Transactional(propagation = REQUIRES_NEW)
    fun addMove(player: Player, currentResult: Int, value: Int, game: Game): Move {
        return moveRepository.save(Move(player = player, value = value, currentResult = currentResult, game = game))
    }

    @Transactional
    fun addFirstMove(player: Player, initialValue: Int, game: Game): Move {
        return moveRepository.save(Move(player = player, value = null, currentResult = initialValue, game = game))
    }

    fun getLastMove(gameId: String): Move? {
        return moveRepository.getLastMove(gameId)
    }

    fun getAllMoves(gameId: String, pageable: Pageable): Page<MoveDTO> {
        return moveRepository.getAllMoves(gameId, pageable).map { it.toDTO() }
    }
}
