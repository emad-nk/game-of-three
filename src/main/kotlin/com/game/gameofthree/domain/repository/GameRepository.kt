package com.game.gameofthree.domain.repository

import com.game.gameofthree.domain.model.Game
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface  GameRepository : JpaRepository<Game, String>{

    @Query(
        nativeQuery = true,
        value = """
            select * from game
            where status = 'WAITING'
            order by created_at
            limit 1
        """,
    )
    fun findOldestGameWithStatusWaiting(): Game?

    @Query(
        nativeQuery = true,
        value = """
            select * from game
            where status = 'PLAYING'
            and id = :id
        """,
    )
    fun findAPlayingGame(id: String): Game?
}
