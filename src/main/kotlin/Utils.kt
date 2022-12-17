import java.io.File
import java.lang.Integer.min
import kotlin.math.sqrt

object Utils {
    fun getLines(src: String): MutableList<String> {
        var list = mutableListOf<String>()
        File("./src/main/resources/$src").forEachLine { list.add(it) }
        return list
    }
}

class Point(var x: Int, var y: Int) {
    operator fun plus(other: Point) = Point(x + other.x, y + other.y)

    operator fun minus(other:Point) = plus(other*-1)
    operator fun times(i: Int) = Point(x*i, y*i)

    operator fun div(i: Int) = Point(x/i, y/i)

    fun clone() = Point(x, y)

    override fun equals(other: Any?): Boolean {
        if (other is Point)
            return x==other.x && y == other.y
        else return super.equals(other)
    }

    override fun hashCode(): Int {
        return x*5000000+y
    }

    override fun toString(): String = "($x, $y)"
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

fun <T> Array<T>.shiftDown(amount: Int, fillValue: T) {
    for (i in 0 until min(this.size-amount, this.size/2)) {
        this[i] = this[i+amount]
    }
    for (i in min(amount, this.size-1) until this.size) {
        this[i] = fillValue
    }
}

fun <T> Array<T>.display(): String {
    var s = "["
    for (i in this.indices) {
        s += this[i]
        if (i < this.size-1)
            s += ","
    }
    return "$s]"
}

fun String.split(i: Int): List<String> = listOf (
    this.substring(0..i),
    this.substring(i+1 until this.length)
        )

fun <T, R> T.convert(op: (T) -> R) = op(this)
