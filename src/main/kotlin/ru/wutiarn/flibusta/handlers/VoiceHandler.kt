package ru.wutiarn.flibusta.handlers

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.request.GetFile
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class VoiceHandler(val asrToken: String, val bot: TelegramBot, val httpClient: OkHttpClient) {

    val clientId = "676bc0734147440490c3312bd36b4154"

    fun processVoiceMessage(msg: Message) {
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
        val respString = asrResp.body().string()
    }
}