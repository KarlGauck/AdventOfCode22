import java.io.File

object Utils {
    fun getLines(src: String): MutableList<String> {
        var list = mutableListOf<String>()
        File("./src/main/resources/$src").forEachLine { list.add(it) }
        return list
    }
}

class Point(var x: Int, var y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)
    operator fun plusAssign(other: Point) {
        this.x += other.x
        this.y += other.y
    }

    fun clone() = Point(x, y)

    override fun equals(other: Any?): Boolean {
        if (other is Point)
            return x==other.x && y == other.y
        else return super.equals(other)
    }

    override fun hashCode(): Int {
        return x*10000+y
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

inline fun <reified T> Array<Array<T>>.xToY() = Array(this[0].size){ ox-> Array(this.size) { oy -> this[oy][ox] } }

fun String.split(i: Int): List<String> = listOf (
    this.substring(0..i),
    this.substring(i+1 until this.length)
        )

fun Int.divisibleBy(i: Int) {
    when (i) {
        17 -> {

        }
    }
}