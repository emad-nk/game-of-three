package com.game.gameofthree.domain.repository

import com.game.gameofthree.domain.model.Move
import org.springframework.data.jpa.repository.JpaRepository

interface  MoveRepository : JpaRepository<Move, String>
