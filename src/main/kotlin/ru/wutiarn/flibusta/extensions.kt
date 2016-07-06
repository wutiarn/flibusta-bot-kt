package ru.wutiarn.flibusta

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.request.SendMessage

fun TelegramBot.sendText(chatId: Long, text: String) {
    this.execute(SendMessage(chatId, text))
}