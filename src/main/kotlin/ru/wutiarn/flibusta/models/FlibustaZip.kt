package ru.wutiarn.flibusta.models

import java.io.InputStream
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

private val regex = ".*-(\\d+)-(\\d+).zip".toRegex()

class FlibustaZip(val path: Path) {

    val range: IntRange
    val zipFile: ZipFile

    init {
        val regexResult = regex.find(path.toString())
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