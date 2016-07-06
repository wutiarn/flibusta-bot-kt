import ru.wutiarn.flibusta.models.FlibustaStorage
import java.io.File
import java.nio.file.Paths

fun main(args: Array<String>) {
    val flibustaStorage = FlibustaStorage(Paths.get("data"))
    val observable = flibustaStorage.getBook(367300, "epub")
    observable.subscribe {
        File("q.epub").writeBytes(it)
    }

    Thread.sleep(1000*1000)
}

