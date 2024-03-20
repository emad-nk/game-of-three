package com.game.gameofthree.common

fun findBestDivisibleBy3(number: Int): Int {
    val remainder = number % 3
    return if (remainder == 0) {
        number
    } else if (remainder == 1) {
        number - 1
    } else {
        number + 1
    }
}
