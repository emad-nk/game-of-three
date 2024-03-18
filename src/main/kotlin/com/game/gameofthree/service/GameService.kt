package com.game.gameofthree.service

import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.domain.repository.GameRepository
import org.springframework.stereotype.Service

@Service
class GameService(
    private val gameRepository: GameRepository,
    private val moveService: MoveService,
    private val playerService: PlayerService,
) {

//    fun start(playerId: String, initialValue: Int): GameDTO {
//
//    }
}
