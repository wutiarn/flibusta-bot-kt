package ru.wutiarn.flibusta.config

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.TelegramBotAdapter
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
open class TelegramConfig(@Value("\${TELEGRAM_TOKEN}") private val telegramToken: String) {
    @Bean
    open fun bot(): TelegramBot {
        val httpClient = OkHttpClient().newBuilder()
                .readTimeout(65, TimeUnit.SECONDS)
                .build()
        return TelegramBotAdapter.buildCustom(telegramToken, httpClient)
    }
}