package ru.wutiarn.flibusta.models

import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

class FlibustaStorage(baseDir: Path) {

    private val zips: List<FlibustaZip>

    init {
        zips = Files.newDirectoryStream(baseDir)
                .map { FlibustaZip(it) }
                .toList()
    }

    fun getBook(id: Int): ByteArray {
        return zips.firstOrNull { it.contains(id) }
                ?.getBookStream(id)
                ?.readBytes() ?: throw FileNotFoundException()
    }

    fun getBook(id: Int, format: String): ByteArray {
        val bookBytes = getBook(id)

        if (format == "fb2") return bookBytes

        val inTempFile = File.createTempFile(UUID.randomUUID().toString(), ".fb2")
        val outTempFile = File.createTempFile(UUID.randomUUID().toString(), ".$format")
        inTempFile.writeBytes(bookBytes)

        Runtime.getRuntime().exec(arrayOf("ebook-convert", inTempFile.absolutePath, outTempFile.absolutePath)).waitFor()

        inTempFile.delete()
        val res = outTempFile.readBytes()
        outTempFile.delete()
        return res
    }

}