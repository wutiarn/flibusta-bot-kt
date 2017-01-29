package ru.wutiarn.flibusta

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import ru.wutiarn.flibusta.bot.TelegramEventsRouter
import kotlin.concurrent.thread

@SpringBootApplication
open class FlibustaBotApplication {
    @Autowired
    open fun runBot(bot: TelegramEventsRouter) {
        thread { bot.run() }
    }
}