package com.game.gameofthree.configuration

import com.game.gameofthree.liveupdate.LiveUpdateService
import com.game.gameofthree.property.PusherProperties
import com.pusher.rest.Pusher
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableConfigurationProperties(PusherProperties::class)
@EnableAsync
class PusherConfiguration {

    @Bean
    fun pusher(pusherProperties: PusherProperties): Pusher {
        val pusher = Pusher(pusherProperties.appId, pusherProperties.key, pusherProperties.secret)
        pusher.setCluster(pusherProperties.cluster)
        pusher.setEncrypted(true)
        return pusher
    }

    @Bean
    fun liveUpdateService(pusher: Pusher, pusherProperties: PusherProperties): LiveUpdateService {
        return LiveUpdateService(pusher, pusherProperties.channelPrefixes)
    }
}
