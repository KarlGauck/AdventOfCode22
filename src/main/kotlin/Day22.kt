import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Day22 {

    val A = a(50, 0)
    val B = a(99, 0)
    val C = a(100, 0)
    val D = a(149, 0)
    val E = a(149, 49)
    val F = a(100, 49)
    val G = a(99, 50)
    val H = a(99, 99)
    val I = a(99, 100)
    val J = a(99, 149)
    val K = a(50, 149)
    val L = a(49, 150)
    val M = a(49, 199)
    val N = a(0, 199)
    val O = a(0, 150)
    val P = a(0, 149)
    val Q = a(0, 100)
    val R = a(49, 100)
    val S = a(50, 99)
    val T = a(50, 50)
    val U  = a(50, 49)

    val AB = Edge(A, B, Dir.DOWN)
    val CD = Edge(C, D, Dir.DOWN)
    val DE = Edge(D, E, Dir.LEFT)
    val FE = Edge(F, E, Dir.UP)
    val GH = Edge(G, H, Dir.LEFT)
    val JI = Edge(J, I, Dir.LEFT)
    val KJ = Edge(K, J, Dir.UP)
    val LM = Edge(L, M, Dir.LEFT)
    val NM = Edge(N, M, Dir.UP)
    val ON = Edge(O, N, Dir.RIGHT)
    val PQ = Edge(P, Q, Dir.RIGHT)
    val QR = Edge(Q, R, Dir.DOWN)
    val TS = Edge(T, S, Dir.RIGHT)
    val AU = Edge(A, U, Dir.RIGHT)

    val edges = listOf(
        AB, CD, DE, FE, GH, JI, KJ, LM, NM, ON, PQ, QR, TS, AU
    )

    val edgePairs = listOf(
        Pair(AB, ON),
        Pair(CD, NM),
        Pair(DE, JI),
        Pair(KJ, LM),
        Pair(PQ, AU),
        Pair(QR, TS),
        Pair(FE, GH)
    )

    fun readGrid(src: String): Array<Array<Tile>> {
        val lines = Utils.getLines(src)
        val arrayWidth = lines.subList(0, lines.size-2).maxOf { it.length }
        println(lines.size-2)
        return  Array(lines.size-2) { y ->
            Array(arrayWidth) { x ->
                if (x in lines[y].indices)
                    when (lines[y][x]) {
                        '.' -> Tile.OPEN
                        '#' -> Tile.WALL
                        else -> Tile.VOID
                    }
                else Tile.VOID
            }
        }.xToY()
    }

    fun readMov(src: String): String = Utils.getLines(src).last()

    var printed = false
    val PRINT_GRID = false


    private fun wrapPart1(pos: Pos, dir: Dir, grid: Array<Array<Tile>>): Pair<Pos ,Dir> {
        val wrapDir = dir.left.left
        var pos = pos
        while (true) {
            val nextPos = (pos vectorPlus wrapDir.move)!!
            if (nextPos !in grid || grid[nextPos] == Tile.VOID)
                return Pair(pos, dir)
            pos = nextPos
        }
    }

    private fun wrapPart2(pos: Pos, dir: Dir, grid: Array<Array<Tile>>): Pair<Pos ,Dir> {
        val edge = edges.filter { it.containsPos(pos) && it.dir.left.left == dir }[0]
        val edgeIndex = edge.edgeIndex(pos)!!
        val otherEdge = edgePairs.filter { edge in it }[0].other(edge)
        println(otherEdge.posAtIndex(edgeIndex))
        return Pair(otherEdge.posAtIndex(edgeIndex), otherEdge.dir)
    }

    fun part(part: Int) {
        val src = "22"
        val grid = readGrid(src)
        val mov = readMov(src).segmentByCharSet("RL", "0123456789")

        var pos = arrayOf(grid.xToY()[0].indexOfFirst { it == Tile.OPEN }, 0)
        var dir = Dir.RIGHT

        val findWrapPos: () -> Pair<Pos, Dir> = when (part) {
            1 -> { { wrapPart1(pos, dir, grid) } }
            2 -> { { wrapPart2(pos, dir, grid) } }
            else -> return
        }

        printGrid(grid, pos, dir)
        for (moveWI in mov.withIndex()) {
            val move = moveWI.value

            var next = mov.getOrNull(moveWI.index+1)
            if (move.toIntOrNull() != null)
                for (wI in 0 until move.toInt())  {
                    var nextPos = (pos vectorPlus dir.move)!!
                    var nextDir = dir
                    if (nextPos !in grid || grid[nextPos] == Tile.VOID) {
                        nextPos = findWrapPos().first
                        nextDir = findWrapPos().second
                    }
                    if (grid[nextPos] == Tile.OPEN) {
                        pos = nextPos
                        dir = nextDir
                    }
                    printGrid(grid, pos, dir, "${wI+1}/${move.toInt()} -> $next")
                }

            else for (cWI in move.withIndex()) {
                val c = cWI.value
                next = move.getOrNull(cWI.index+1).toString()
                val olddir = dir
                when(c) {
                    'R' -> dir = dir.right
                    'L' -> dir = dir.left
                }
                printGrid(grid, pos, dir, "$c -> $next     <>     $c::  $olddir  ->  $dir")
            }
            println(move)

        }

        pos = (pos vectorPlus arrayOf(1, 1))!!
        println(1000*pos.y + 4*pos.x + dir.num)
    }


    fun printGrid(grid: Array<Array<Tile>>, pos: Pos, dir: Dir, msg: String = "") {
        if (!PRINT_GRID)
            return
        val size = 10
        if (printed)
            (-size .. size+1).forEach { _ -> print("\u001B[A") }
        for (y in pos.y-size..pos.y+size) {
            for (x in pos.x-size..pos.x+size) {
                if (arrayOf(x, y) !in grid)
                    print("  ")
                else if (pos.contentEquals(arrayOf(x, y)))
                    print("${dir.c} ")
                else print(when (grid[x][y]) {
                    Tile.OPEN -> ". "
                    Tile.WALL -> "# "
                    else -> "  "
                })
            }
            println()
        }
        println("---------------------- $msg")
        printed = true
        Thread.sleep(400)
    }

}

enum class Tile {
    OPEN,
    WALL,
    VOID
}

enum class Dir(val num: Int, val move: Pos, val c: Char) {
    RIGHT(0, arrayOf(1, 0), '>'),
    DOWN(1, arrayOf(0, 1), 'v'),
    LEFT(2, arrayOf(-1, 0), '<'),
    UP(3, arrayOf(0, -1), '^');

    val left
        get() = when (this) {
            RIGHT -> UP
            DOWN -> RIGHT
            LEFT -> DOWN
            UP -> LEFT
        }

    val right
        get() = when (this) {
            RIGHT -> DOWN
            DOWN -> LEFT
            LEFT -> UP
            UP -> RIGHT
        }
}

class Edge (val start: Pos, val end: Pos, val dir: Dir) {

    val dx = if (end.x == start.x) 0 else (end.x-start.x)/abs(end.x-start.x)
    val dy = if (end.y == start.y) 0 else (end.y-start.y)/abs(end.y-start.y)
    val maxX = max(start.x, end.x)
    val minX = min(start.x, end.x)
    val maxY = max(start.y, end.y)
    val minY = min(start.y, end.y)

    fun containsPos(pos: Pos): Boolean = if (dx == 0) pos.x == start.x && pos.y in minY..maxY
        else pos.y == start.y && pos.x in minX..maxX

    fun edgeIndex(pos: Pos): Int? {
        if (!containsPos(pos))
            return null

        var curr = start
        var index = 0
        while (!curr.contentEquals(pos)) {
            index -=-1
            curr = (curr vectorPlus a(dx,dy))!!
        }

        return index
    }

    fun posAtIndex(index: Int): Pos = (start vectorPlus (a(dx, dy) vectorTimes index))!!

}