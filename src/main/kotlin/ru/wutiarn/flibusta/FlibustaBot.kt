package ru.wutiarn.flibusta

import com.pengrad.telegrambot.TelegramBotAdapter
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.GetUpdates
import com.pengrad.telegrambot.request.SendDocument
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.TimeUnit

class FlibustaBot(telegramToken: String, val libPath: String) {
    val supportedFormats = listOf("mobi", "epub", "pdf", "fb2")
    val idRegex = ".*?(\\d{4,7}).*".toRegex()

    var flibustaStorage: FlibustaStorage = initStorage()
    val httpClient = OkHttpClient().newBuilder()
            .readTimeout(65, TimeUnit.SECONDS)
            .build()
    val bot = TelegramBotAdapter.buildCustom(telegramToken, httpClient)

    val requestedBooks = mutableMapOf<Long, WeakReference<HashSet<Int>>>()


    fun run() {
        var lastUpdateId = 0
        while (true) {
            val updates = bot.execute(GetUpdates().timeout(60).offset(lastUpdateId)).updates() ?: continue
            for (update in updates) {
                lastUpdateId = update.updateId() + 1
                val message = update.message()
                message?.text()?.let { processMessage(message) }
            }
        }
    }

    fun initStorage(): FlibustaStorage {
        return FlibustaStorage(Paths.get(libPath))
    }

    fun processMessage(msg: Message) {
        val chatId = msg.chat().id()

        val from = msg.from()
        log("[${from.id()}] [Received] @${from.username()}: ${msg.text()}")

        val chatRequestedBooksQueue = requestedBooks[chatId]?.get() ?: let {
            val queue = HashSet<Int>(10)
            requestedBooks[chatId] = WeakReference(queue)
            queue
        }

        var text = msg.text()

        if (text.startsWith("/")) text = text.slice(1..text.lastIndex)

        if (text == "clear") {
            requestedBooks.clear()
            bot.sendText(chatId, "Queue cleared")
            return
        }

        if (text == "update") {
            if (chatId != 43457173L) {
                bot.sendText(chatId, "You don't have access to this feature")
            } else {
                bot.sendText(chatId, "Scan initiated. Old zips count: ${flibustaStorage.zipCount()}")
                flibustaStorage = initStorage()
                bot.sendText(chatId, "Scan finished. New zips count: ${flibustaStorage.zipCount()}")
            }
            return
        }

        val entries = text.split("[ \n]".toRegex())
        val format = entries.firstOrNull() { it in supportedFormats }

        val ids = entries.filter { idRegex.matches(it) }
                .map { idRegex.find(it)!!.groups[1]!!.value }
                .map { it.toInt() }
                .toSet()

        if (ids.size + chatRequestedBooksQueue.size > 10) {
            bot.sendText(chatId, "You can't add to queue more than 10 elements." +
                    " You can clear queue with /clear command")
            return
        }

        chatRequestedBooksQueue.addAll(ids)

        format ?: let {

            val replyMarkup = ReplyKeyboardMarkup(arrayOf("epub", "mobi", "fb2", "pdf", "clear"))

            bot.sendText(chatId, "Requested: ${chatRequestedBooksQueue.joinToString(", ")}. " +
                    "Select desired format or clear to cancel", replyMarkup)
            return
        }

        bot.sendText(chatId, "Okay, ${chatRequestedBooksQueue.joinToString(", ")}. Give me a moment")

        chatRequestedBooksQueue.forEach { id ->
            flibustaStorage.getBook(id, format).subscribe({
                bot.sendText(chatId, "$id: done. Sending.")
                val filename = "$id.$format"
                log("[${from.id()}] Sending $filename")
                bot.execute(SendDocument(chatId, it).fileName(filename))
            }, {
                bot.sendText(chatId, "$id: failed. Not found or an error occurred.")
            })
        }

        chatRequestedBooksQueue.clear()
    }
}

