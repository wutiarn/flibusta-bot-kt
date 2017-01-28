package ru.wutiarn.flibusta

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import kotlin.concurrent.thread

@SpringBootApplication
open class FlibustaBotApplication {
    @Autowired
    open fun runBot(bot: FlibustaBot) {
        thread { bot.run() }
    }
}