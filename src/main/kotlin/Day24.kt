import kotlin.math.abs

object Day24 {

    var upTornados = Array(0) { Array(0) {false} }
    var downTornados = Array(0) { Array(0) {false} }
    var leftTornados = Array(0) { Array(0) {false} }
    var rightTornados = Array(0) { Array(0) {false} }
    var visited = Array(0) { Array(0) { false } }

    var height = 0
    var width = 0

    fun tornado(x: Int, y: Int) = upTornados[x][y] || downTornados[x][y] || leftTornados[x][y] || rightTornados[x][y]

    fun tornadoStep() {

        for (x in upTornados.indices) {
            val up = upTornados[x][0]
            val down = downTornados[x][height-1]

            for (y in upTornados[x].indices) upTornados[x, y] = if (y == height-1) up else upTornados[x][y+1]
            for (y in downTornados[x].indices.reversed()) downTornados[x][y] = if (y == 0) down else downTornados[x][y-1]
        }

        for (y in leftTornados[0].indices) {
            val left = leftTornados[0][y]
            val right = rightTornados[width-1][y]

            for (x in leftTornados.indices) leftTornados[x, y] = if (x == width-1) left else leftTornados[x+1][y]
            for (x in rightTornados.indices.reversed()) rightTornados[x, y] = if (x == 0) right else rightTornados[x-1][y]
        }

    }

    fun part1() {
        val lines = Utils.getLines("24")
        height = lines.size-2
        width = lines[0].length-2

        upTornados = Array(width) { x-> Array(height) { y-> lines[y+1][x+1] == '^' } }
        downTornados = Array(width) { x-> Array(height) { y-> lines[y+1][x+1] == 'v' } }
        leftTornados = Array(width) { x-> Array(height) { y-> lines[y+1][x+1] == '<'} }
        rightTornados = Array(width) { x-> Array(height) { y-> lines[y+1][x+1] == '>' } }
        visited = Array(width) { Array(height) { false } }

        var time = 1
        while (true) {
            if (!tornado(0, 0))
                visited[0, 0] = true

            tornadoStep()
            time ++

            val newvisited = Array(width) { Array(height) { false } }
            for (x in visited.indices) {
                for (y in visited[x].indices) {
                    if (!visited[x][y])
                        continue
                    forDeltaXY(1, 1) loop@{ dx, dy ->
                        if (abs(dx) + abs(dy) > 1)
                            return@loop false
                        if (x + dx !in visited.indices || y + dy !in visited[x+dx].indices)
                            return@loop false

                        if (!tornado(x+dx, y+dy))
                            newvisited[x+dx][y+dy] = true

                        false
                    }
                }
            }
            visited = newvisited

            if (visited[width-1][height-1]) {
                println(time+1)
                break
            }

        }
    }

    fun part2() {
        val lines = Utils.getLines("24")
        height = lines.size-2
        width = lines[0].length-2

        upTornados = Array(width) { x-> Array(height) { y-> lines[y+1][x+1] == '^' } }
        downTornados = Array(width) { x-> Array(height) { y-> lines[y+1][x+1] == 'v' } }
        leftTornados = Array(width) { x-> Array(height) { y-> lines[y+1][x+1] == '<'} }
        rightTornados = Array(width) { x-> Array(height) { y-> lines[y+1][x+1] == '>' } }
        visited = Array(width) { Array(height) { false } }

        var time = 1

        var start = a(0, 0)
        var end = a(width-1, height-1)
        var reached = 0
        while (true) {
            if (!tornado(start[0], start[1]))
                visited[start[0], start[1]] = true

            tornadoStep()
            time ++

            val newvisited = Array(width) { Array(height) { false } }
            for (x in visited.indices) {
                for (y in visited[x].indices) {
                    if (!visited[x][y])
                        continue
                    forDeltaXY(1, 1) loop@{ dx, dy ->
                        if (abs(dx) + abs(dy) > 1)
                            return@loop false
                        if (x + dx !in visited.indices || y + dy !in visited[x+dx].indices)
                            return@loop false

                        if (!tornado(x+dx, y+dy))
                            newvisited[x+dx][y+dy] = true

                        false
                    }
                }
            }
            visited = newvisited

            if (visited[end[0]][end[1]]) {
                if (reached == 2) {
                    println(time)
                    break
                }
                reached++
                time ++
                tornadoStep()
                visited = Array(width) { Array(height) { false } }
                start = end.apply { end = start }
            }

        }
    }

}