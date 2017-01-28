package ru.wutiarn.flibusta

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
open class FlibustaBotRunner(private val bot: FlibustaBot) {
    @Async
    open fun run() {
        bot.run()
    }
}