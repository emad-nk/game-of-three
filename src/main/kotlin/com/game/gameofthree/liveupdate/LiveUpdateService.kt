package com.game.gameofthree.liveupdate

import com.pusher.rest.Pusher
import com.pusher.rest.data.Result.Status.SUCCESS
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Async

/**
 * Pusher.com based [LiveUpdateService] implementation.
 * Sends websocket message via pusher.com service
 */
open class LiveUpdateService(private val pusher: Pusher, private val prefixes: List<String>) {

    @Async
    open fun triggerGameUpdate(liveUpdate: LiveUpdate) {
        val channelName = liveUpdate.channelName
        val eventName = liveUpdate.eventName

        for (prefix in prefixes) {
            val channel = "$prefix-$channelName"

            logger.info("Sending notification $eventName to channel $channel")
            val result = pusher.trigger(channel, eventName, liveUpdate.updateDTO)
            if (result.status != SUCCESS) {
                logger.error { "Failure pushing event name $eventName to the channel $channelName" }
            }
            logger.info { "Result [message=${result.message}][status=${result.status}]" }
            logger.info { "Game update: ${liveUpdate.updateDTO}" }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
