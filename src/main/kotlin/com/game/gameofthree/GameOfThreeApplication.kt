package com.game.gameofthree

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GameOfThreeApplication

fun main(args: Array<String>) {
    runApplication<GameOfThreeApplication>(*args)
}
