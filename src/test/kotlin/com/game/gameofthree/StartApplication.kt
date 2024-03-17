package com.game.gameofthree

import org.springframework.boot.builder.SpringApplicationBuilder

/**
 * Entry point for starting application locally.
 */
class StartApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplicationBuilder(GameOfThreeApplication::class.java)
                .profiles("local")
                .initializers(Initializer())
                .run(*args)
        }
    }
}
