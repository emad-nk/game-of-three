package com.game.gameofthree.common

import com.game.gameofthree.domain.model.Move
import com.game.gameofthree.domain.model.Player
import com.game.gameofthree.exception.WrongPlayerException
import com.game.gameofthree.exception.WrongValueException

fun Int.validateValue() {
    if (this < -1 || this > 1) {
        throw WrongValueException("You are only allowed to input 1, 0 or -1 as an input value")
    }
}

fun Int.validateInitialValue() {
    if (this <= 2 || this > 1000) {
        throw WrongValueException("Initial value should be greater than 2 and less than 1000")
    }
}

fun Player.validateIfUserIsAllowedToMove(lastMove: Move) {
    if (lastMove.player == this) {
        throw WrongPlayerException("Player ${this.username} has already played, other player should play now")
    }
}
