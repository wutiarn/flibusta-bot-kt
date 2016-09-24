package ru.wutiarn.flibusta

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ReplyKeyboardHide
import com.pengrad.telegrambot.request.SendMessage
import java.time.Instant

fun TelegramBot.sendText(chatId: Long, text: String, replyMarkup: Keyboard? = ReplyKeyboardHide()) {
    log("[$chatId] [Reply] $text")
    this.execute(SendMessage(chatId, text).replyMarkup(replyMarkup))
}

fun log(msg: String) {
    println("[${Instant.now()}] $msg")
}