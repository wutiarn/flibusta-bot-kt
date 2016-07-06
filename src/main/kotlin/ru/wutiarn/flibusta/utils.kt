package ru.wutiarn.flibusta

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage
import java.time.Instant

fun TelegramBot.sendText(chatId: Long, text: String) {
    log("[$chatId] [Reply] $text")
    this.execute(SendMessage(chatId, text))
}

fun log(msg: String) {
    println("[${Instant.now()}] $msg")
}