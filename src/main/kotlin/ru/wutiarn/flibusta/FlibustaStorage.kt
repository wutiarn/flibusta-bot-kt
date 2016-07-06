package ru.wutiarn.flibusta

import rx.Observable
import rx.schedulers.Schedulers
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile


class FlibustaStorage(baseDir: Path) {
    private val zips: List<FlibustaZip>

    init {
        zips = Files.newDirectoryStream(baseDir)
                .map { FlibustaZip(it) }
                .toList()
    }

    private fun getBook(id: Int): Observable<ByteArray> {
        return Observable.just(1)
                .observeOn(Schedulers.io())
                .map {
                    println("Start on ${Thread.currentThread().name}")
                    zips.first { it.contains(id) }
                            .getBookStream(id)
                            .readBytes()
                }
    }

    fun getBook(id: Int, format: String): Observable<ByteArray> {
        val bookObservable = getBook(id)

        if (format == "fb2") return bookObservable

        return bookObservable
                .observeOn(Schedulers.computation())
                .map {
                    println("Convert on ${Thread.currentThread().name}")
                    val inTempFile = File.createTempFile(UUID.randomUUID().toString(), ".fb2")
                    val outTempFile = File.createTempFile(UUID.randomUUID().toString(), ".$format")
                    inTempFile.writeBytes(it)

                    Runtime.getRuntime().exec(arrayOf("ebook-convert", inTempFile.absolutePath, outTempFile.absolutePath)).waitFor()

                    inTempFile.delete()
                    val res = outTempFile.readBytes()
                    outTempFile.delete()
                    res
                }
    }

}

class FlibustaZip(path: Path) {
    companion object {
        private val zipRegex = ".*-(\\d+)-(\\d+).zip".toRegex()
    }

    val range: IntRange
    val zipFile: ZipFile

    init {
        val regexResult = zipRegex.find(path.toString())
        regexResult ?: throw IllegalArgumentException()
        range = Integer.parseInt(regexResult.groups[1]!!.value)..Integer.parseInt(regexResult.groups[2]!!.value)

        zipFile = ZipFile(path.toFile())
    }

    fun contains(id: Int): Boolean {
        if (id in range) {
            return (getEntry(id) != null)
        }
        return false
    }

    private fun getEntry(id: Int): ZipEntry? {
        return zipFile.getEntry("$id.fb2")
    }

    fun getBookStream(id: Int): InputStream {
        return zipFile.getInputStream(getEntry(id))
    }
}