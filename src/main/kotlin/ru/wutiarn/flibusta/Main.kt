import ru.wutiarn.flibusta.models.FlibustaStorage
import java.nio.file.Paths

fun main(args: Array<String>) {
    val flibustaStorage = FlibustaStorage(Paths.get("data"))
    val convertedBook = flibustaStorage.getBook(367300, "mobi")
}

