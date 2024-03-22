package com.game.gameofthree.liveupdate

import com.game.gameofthree.controller.response.GameDTO
import com.pusher.rest.Pusher
import com.pusher.rest.data.Result.Status.SUCCESS
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async

/**
 * Pusher.com based [LiveUpdateService] implementation.
 * Sends websocket message via pusher.com service
 */
open class LiveUpdateService(
    private val pusher: Pusher,
    private val prefixes: List<String>,
) {

    @Async
    open fun triggerGameUpdate(gameDTO: GameDTO, gameEvent: GameEvent) {
        for (prefix in prefixes) {
            val channel = "$prefix-$GAME_UPDATE${gameDTO.id}"

            logger.info("Sending notification $gameEvent to channel $channel")
            val result = pusher.trigger(channel, gameEvent.name, gameDTO)
            if (result.status != SUCCESS) {
                logger.error { "Failure pushing game event ${gameEvent.name} to the channel $channel" }
            }
            logger.info { "Result [message=${result.message}][status=${result.status}]" }
            logger.info { "Game update: $gameDTO, with the event $gameEvent" }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
        private const val GAME_UPDATE = "game-update-"
    }
}
