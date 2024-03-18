package com.game.gameofthree.domain.repository

import com.game.gameofthree.domain.model.Move
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface  MoveRepository : JpaRepository<Move, String> {

    @Query(
        nativeQuery = true,
        value = """
            select * from move
            where game_id = :gameId
            order by timestamp desc
            limit 3
        """,
    )
    fun getLastThreeMoves(gameId: String): List<Move>

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
}
