package com.game.gameofthree.configuration

import com.game.gameofthree.threadpool.ThreadPoolNames.GAME_UPDATE
import com.game.gameofthree.threadpool.ThreadPoolSubmitter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import java.util.concurrent.Executor

@Configuration
@EnableAsync
class SpringAsyncConfig(
    private val threadPoolSubmitter: ThreadPoolSubmitter,
) {
    @Bean(name = [GAME_UPDATE_EXECUTOR])
    fun gameUpdateExecutor(): Executor = threadPoolSubmitter.getThreadPool(GAME_UPDATE)

    companion object {
        const val GAME_UPDATE_EXECUTOR = "$GAME_UPDATE-executor"
    }
}
