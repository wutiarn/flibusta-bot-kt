package ru.wutiarn.flibusta

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
open class FlibustaBotApplication {
    @Autowired
    open fun runBot(runner: FlibustaBotRunner) {
        runner.run()
    }
}