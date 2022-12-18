import kotlin.math.abs

object Day12 {
    var startPos: Point = Point(0, 0)
    var endPos: Point = Point(0, 0)

    var initialized = false
    var grid: Array<Array<Int>> = a()
    var dirs: Array<Array<MutableSet<Point>>> = a()
    var parents = mutableMapOf<Point, Pair<Point, Int>>(
        )

    fun readInput(src: String): Array<Array<Int>> {
        return Utils.getLines(src).mapIndexed{ y, it -> Array(it.length) { x ->
            if (it[x] == 'S')
                startPos = Point(x, y)
            if (it[x] == 'E')
                endPos = Point(x, y)
            if (it[x] == 'S') 'a'.toInt() else if (it[x] == 'E') 'z'.toInt() else it[x].toInt()}
        }.toTypedArray().xToY()
    }

    fun initialize(reversed: Boolean = false) {
        if (!initialized) {
            grid = readInput("12.txt")
            dirs = grid.mapIndexed { xI, x -> x.mapIndexed { yI, y ->
                val set = mutableSetOf<Point>()
                val current = y
                for (dx in -1..1)
                    for (dy in -1..1) {
                        if (abs(dx) == abs(dy) || xI+dx !in grid.indices || yI+dy !in grid[xI].indices)
                            continue
                        val neighbour = grid[xI+dx][yI+dy]
                        if (!reversed) {
                            if (neighbour-current < 2)
                                set.add(Point(dx, dy))
                        }
                        else if (current-neighbour < 2)
                            set.add(Point(dx, dy))
                    }
                set
            }.toTypedArray() }.toTypedArray()
            initialized = true
        }
    }

    fun shortestPath(start: Point, end: Point, allPoints: Boolean = false) {
        initialize()
        val visited = mutableSetOf<Point>()
        val marked = mutableSetOf<Point>()
        parents = mutableMapOf(
            Pair(start, Pair(start, 0))
        )
        var current = start
        while(true) {
            for (dir in dirs[current.x][current.y]) {
                marked.add(current+dir)
                if (parents[current+dir] != null)
                    if(parents[current+dir]!!.second > parents[current]!!.second + 1)
                        parents[current+dir] = Pair(current, parents[current]!!.second+1)
                    else;
                else parents.put(current+dir, Pair(current, parents[current]!!.second+1))
            }
            visited.add(current)
            val remaining = marked.filter { it !in visited }
            if (current == end && !allPoints)
                break
            else if (remaining.isEmpty())
                break
            current = remaining.sortedBy { parents[it]!!.second }[0]
            marked.remove(current)
        }

    }

    fun part1() {
        shortestPath(startPos, endPos)

        var current = endPos
        var steps = 0
        while (true) {
            current = parents[current]!!.first
            steps-=-1
            if (current == startPos)
                break
        }
        println(steps)

    }

    fun part2() {
        initialize(true)
        val startPoints = mutableListOf<Point>()
        for (x in grid.indices) {
            for (y in grid[x].indices) {
                if (grid[x][y] == 'a'.toInt())
                    startPoints.add(Point(x, y))
            }
        }
        shortestPath(endPos, Point(0, 0), true)
        var minSteps = Int.MAX_VALUE
        points@for (p in startPoints) {
            var current = p
            var steps = 0
            while (true) {
                if (parents[current] == null)
                    continue@points
                current = parents[current]!!.first
                steps-=-1
                if (current == endPos)
                    break
            }
            if (steps < minSteps) {
                minSteps = steps
            }
        }
        println("final: $minSteps")

    }

}