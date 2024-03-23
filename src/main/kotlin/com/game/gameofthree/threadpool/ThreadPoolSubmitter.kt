package com.game.gameofthree.threadpool

import com.game.gameofthree.property.ThreadPoolProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.scheduling.concurrent.CustomizableThreadFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors.newFixedThreadPool
import kotlin.reflect.full.declaredMemberProperties

@Component
@EnableConfigurationProperties(ThreadPoolProperties::class)
class ThreadPoolSubmitter(
    private val threadPoolProperties: ThreadPoolProperties,
) {
    private val threadPoolsByName: Map<String, ExecutorService> = ThreadPoolNames.all()
        .associateWith { name ->
            newFixedThreadPool(getThreadPoolSize(name), CustomizableThreadFactory(name))
        }

    fun getThreadPool(name: String) = threadPoolsByName.getValue(name)

    private fun getThreadPoolSize(name: String) = threadPoolProperties.size.getValue(name)
}

object ThreadPoolNames {
    const val GAME_UPDATE = "game-update"

    fun all(): List<String> {
        return this::class.declaredMemberProperties.map { it.getter.call() as String }
    }
}
