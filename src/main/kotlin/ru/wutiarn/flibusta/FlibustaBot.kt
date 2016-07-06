package ru.wutiarn.flibusta

import com.pengrad.telegrambot.TelegramBotAdapter
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.GetUpdates
import com.pengrad.telegrambot.request.SendDocument
import okhttp3.OkHttpClient
import java.lang.ref.WeakReference
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.TimeUnit

class FlibustaBot {
    val supportedFormats = listOf("mobi", "epub", "pdf", "fb2")
    val idRegex = ".*?(\\d{4,7}).*".toRegex()

    val flibustaStorage = FlibustaStorage(Paths.get("data"))
    val httpClient = OkHttpClient().newBuilder()
            .readTimeout(65, TimeUnit.SECONDS)
            .build()
    val bot = TelegramBotAdapter.buildCustom("231792146:AAH36rnTv-1xMA-dz3dg2JsWE-o7P5coFa4", httpClient)

    val requestedBooks = mutableMapOf<Long, WeakReference<HashSet<Int>>>()


    fun run() {
        var lastUpdateId = 0
        while (true) {
            for (update in bot.execute(GetUpdates().timeout(60).offset(lastUpdateId)).updates()) {
                lastUpdateId = update.updateId() + 1
                val message = update.message()
                message?.text()?.let { processMessage(message) }
            }
        }
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
        if (text == "/clear") {
            requestedBooks.clear()
            bot.sendText(chatId, "Queue cleared")
            return
        }

        if (text.startsWith("/")) text = text.slice(1..text.lastIndex)

        val entries = text.split(" ")
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
            bot.sendText(chatId, "Requested: ${chatRequestedBooksQueue.joinToString(", ")}. " +
                    "Send me desired format (/epub , /mobi , /fb2 , /pdf) or /clear to clear queue")
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

