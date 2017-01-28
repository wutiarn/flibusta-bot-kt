package ru.wutiarn.flibusta

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.Keyboard
import com.pengrad.telegrambot.model.request.ReplyKeyboardHide
import com.pengrad.telegrambot.request.SendMessage
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("ru.wutiarn.flibusta.utils")

fun TelegramBot.sendText(chatId: Long, text: String, replyMarkup: Keyboard? = ReplyKeyboardHide()) {
    logger.info("[$chatId] [Reply] $text")
    this.execute(SendMessage(chatId, text).replyMarkup(replyMarkup))
}