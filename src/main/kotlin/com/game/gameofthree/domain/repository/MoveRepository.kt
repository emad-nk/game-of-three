package com.game.gameofthree.domain.repository

import com.game.gameofthree.domain.model.Move
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MoveRepository : JpaRepository<Move, String> {

    @Query(
        nativeQuery = true,
        value = """
            select * from move
            where game_id = :gameId
            order by timestamp desc
            limit 1
        """,
    )
    fun getLastMove(gameId: String): Move?

    @Query(
        nativeQuery = true,
        value = """
            select * from move
            where game_id = :gameId
        """,
    )
    fun getAllMoves(gameId: String, pageable: Pageable): Page<Move>
}
