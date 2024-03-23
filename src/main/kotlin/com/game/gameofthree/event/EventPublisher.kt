package com.game.gameofthree.event

import com.game.gameofthree.controller.response.GameDTO
import com.game.gameofthree.liveupdate.GameEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class EventPublisher(
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    fun publishUpdateEvent(gameDTO: GameDTO, gameEvent: GameEvent) {
        applicationEventPublisher.publishEvent(GameUpdateEvent(gameDTO = gameDTO, gameEvent = gameEvent))
    }
}
