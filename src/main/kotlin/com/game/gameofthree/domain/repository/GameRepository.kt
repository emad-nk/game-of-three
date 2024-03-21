package com.game.gameofthree.domain.repository

import com.game.gameofthree.configuration.CacheNames.PLAYING_GAME_BY_ID
import com.game.gameofthree.domain.model.Game
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GameRepository : JpaRepository<Game, String> {

    @CacheEvict(value = [PLAYING_GAME_BY_ID], key = "{#entity.id}")
    override fun <S : Game> save(entity: S): S

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
    @Cacheable(value = [PLAYING_GAME_BY_ID], key = "{#id}", unless = "#result == null")
    fun findAPlayingGame(id: String): Game?
}
