package com.game.gameofthree.common

import kotlin.random.Random

fun findBestDivisibleBy3(number: Int): Int {
    val remainder = number % 3
    return if (remainder == 0) {
        0
    } else if (remainder == 1) {
        -1
    } else {
        1
    }
}

fun getARandomNumberBetweenTwoAndThousand(): Int {
    return Random.nextInt(3, 1001)
}
