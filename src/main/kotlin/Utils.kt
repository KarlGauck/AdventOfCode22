import java.io.File
import java.lang.Integer.min

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

fun forDeltaX(xRadius: Int, operation: (dx: Int) -> Boolean) {
    for (dx in -xRadius..xRadius)
        if (operation(dx)) return
}

fun forDeltaXY(xRadius: Int, yRadius: Int, operation: (dx: Int, dy: Int) -> Boolean) {
    for (dx in -xRadius..xRadius)
        for (dy in -yRadius..yRadius)
            if (operation(dx, dy)) return
}

fun forDeltaXYZ(xRadius: Int, yRadius: Int, zRadius: Int, operation: (dx: Int, dy: Int, dz: Int) -> Boolean) {
    for (dx in -xRadius..xRadius)
        for (dy in -yRadius..yRadius)
            for (dz in -zRadius..zRadius)
                if (operation(dx, dy, dz)) return
}

inline fun <reified T> a(vararg arg: T) = Array(arg.size) {arg[it]}

operator fun <T> Array<T>.invoke(index: Int): T = if (index < 0) this[this.lastIndex-index] else this[index]
operator fun <T> List<T>.invoke(index: Int): T = if (index < 0) this[this.lastIndex-index] else this[index]

operator fun <T> List<T>.get(string: String): T {
    val sliced = string.slice(1 until string.length).toIntOrNull()
    if (sliced != null) {
        return if (sliced < 0) this[this.lastIndex- sliced *-1] else this[sliced]
    }
    else throw IllegalArgumentException()
}

operator fun <T> Array<T>.get(string: String): T {
    val sliced = string.slice(1 until string.length).toIntOrNull()
    if (sliced != null) {
        return if (sliced < 0) this[this.lastIndex- sliced *-1] else this[sliced]
    }
    else throw IllegalArgumentException()
}

fun String.segmentByCharSet(vararg sets: String): List<String> {
    val list = mutableListOf<String>()
    var curr = ""
    var currSet = ""
    for (cWI in this.withIndex()) {
        val c = cWI.value
        val set = sets.firstOrNull { c in it } ?: ""
        if (set != currSet) {
            if (curr != "")
                list += curr
            currSet = set
            curr = ""
        }
        curr += c
        if (cWI.index == this.lastIndex)
            list += curr
    }
    return list
}

infix fun Pos.vectorPlus(other: Array<Int>): Pos? = if (this.size == other.size)
    this.mapIndexed { index: Int, i: Int -> i + other[index] }.toTypedArray()  else null

infix fun Pos.vectorTimes(scalar: Int): Pos = this.map { it * scalar }.toTypedArray()

operator fun <T> Array<Array<T>>.contains(array: Array<Int>): Boolean = array[0] in this.indices && array[1] in this[array[0]].indices

operator fun <T> Array<Array<T>>.get(array: Array<Int>): T = this[array[0]][array[1]]

var Array<Int>.x
    set(value) {
        this[0] = value
    }
    get() = this[0]

var Array<Int>.y
    set(value) {
        this[1] = value
    }
    get() = this[1]

var Array<Int>.xy: Pos
    set(value) {
        this[0] = value[0]
        this[1] = value[1]
    }
    get() = a(this[0], this[1])

typealias Pos = Array<Int>

fun <T> Pair<T, T>.other(element: T): T = if (this.first == element) this.second else this.first

operator fun <T> Pair<T, T>.contains(element: T): Boolean = this.first == element || this.second == element

fun Array<Int>.clone() = this.map { it }

