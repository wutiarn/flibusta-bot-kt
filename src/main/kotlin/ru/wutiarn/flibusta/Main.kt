package ru.wutiarn.flibusta

fun main(args: Array<String>) {
    val telegramToken = getEnv("TELEGRAM_TOKEN")
    val libPath = "/code/data"
    val bot = FlibustaBot(telegramToken, libPath)
    log("Started")
    bot.run()
}

fun getEnv(name: String): String = System.getenv(name)
        ?: throw IllegalArgumentException("You must set $name env before start")