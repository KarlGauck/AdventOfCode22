import java.io.File

object Utils {
    fun getLines(src: String): MutableList<String> {
        var list = mutableListOf<String>()
        File("./src/main/resources/$src").forEachLine { list.add(it) }
        return list
    }
}

fun <T> List<T>.split(predicate: T): List<List<T>> {
    val lists = mutableListOf<MutableList<T>>(mutableListOf())
    var newList = false
    for (t in this) {
        if (predicate != null) {
            if (predicate == t) {
                newList = true
            }
            else {
                if (newList) {
                    newList = false
                    lists.add(mutableListOf())
                }
                lists.last().add(t)
            }
        }
    }
    return lists
}


fun String.split(i: Int): List<String> = listOf (
    this.substring(0..i),
    this.substring(i+1 until this.length)
        )