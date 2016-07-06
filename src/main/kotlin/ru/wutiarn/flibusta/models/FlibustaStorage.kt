package ru.wutiarn.flibusta.models

import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

class FlibustaStorage(baseDir: Path) {

    private val zips: List<FlibustaZip>

    init {
        zips = Files.newDirectoryStream(baseDir)
                .map { FlibustaZip(it) }
                .toList()
    }

    fun getBookStream(id: Int): InputStream? {
        return zips.firstOrNull { it.contains(id) }
                ?.getBookStream(id)
    }

}