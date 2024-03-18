package com.game.gameofthree.domain.repository

import com.game.gameofthree.domain.model.Game
import org.springframework.data.jpa.repository.JpaRepository

interface  GameRepository : JpaRepository<Game, String>
