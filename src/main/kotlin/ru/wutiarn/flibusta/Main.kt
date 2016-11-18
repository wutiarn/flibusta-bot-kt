package ru.wutiarn.flibusta

fun main(args: Array<String>) {
    val telegramToken = getEnv("TELEGRAM_TOKEN")
    val asrToken = getEnv("ASR_TOKEN")
    val libPath = "data"
    val bot = FlibustaBot(telegramToken, asrToken, libPath)
    log("Started")
    bot.run()
}

fun getEnv(name: String): String = System.getenv(name)
        ?: throw IllegalArgumentException("You must set $name env before start")