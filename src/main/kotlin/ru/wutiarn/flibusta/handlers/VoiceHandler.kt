package ru.wutiarn.flibusta.handlers

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.GetFile
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.dom4j.io.SAXReader
import ru.wutiarn.flibusta.sendText

class VoiceHandler(val asrToken: String, val bot: TelegramBot, val httpClient: OkHttpClient) {

    val clientId = "676bc0734147440490c3312bd36b4154"

    fun processVoiceMessage(msg: Message) {
        val chatId = msg.chat().id()

        val fileId = msg.voice().fileId()
        val fileMeta = bot.execute(GetFile(fileId)).file()
        val fileURL = bot.getFullFilePath(fileMeta)

        val fileRequest = Request.Builder().url(fileURL).build()
        val fileResp = httpClient.newCall(fileRequest).execute()
        val fileBytes = fileResp.body().bytes()

        val asrRequest = Request.Builder()
                .url("https://asr.yandex.net/asr_xml?uuid=$clientId&key=$asrToken&topic=queries")
                .post(RequestBody.create(MediaType.parse("audio/ogg;codecs=opus"), fileBytes))
                .build()

        val asrResp = httpClient.newCall(asrRequest).execute()
        val respStream = asrResp.body().byteStream()
        val xmlReader = SAXReader().read(respStream)
        val results = xmlReader.rootElement.elements()

        if (results.isEmpty()) {
            bot.sendText(chatId, "Unrecognized")
            return
        }

        val recognisedText = results[0].text
        val confidence = results[0].attribute("confidence").data

        bot.sendText(chatId, "Recognised ($confidence): $recognisedText")
    }
}