object Day8 {

    fun part1() {
        val lines = Utils.getLines("8.txt")
        val trees = Array(lines.size) { IntArray(lines[0].length) }
        lines.forEachIndexed { i, it -> trees[i] = it.toCharArray().map { it.digitToInt() }.toIntArray() }

        val treeLiberties = Array(lines.size) { Array(lines[0].length) { 4 } }

        for (y in 0 until lines.size) {
            var currentMax = -1
            for (x in 0 until lines[0].length) {
                if (trees[x][y] <= currentMax) {
                    treeLiberties[x][y] --
                } else {
                    currentMax = trees[x][y]
                }
            }
        }

        for (y in 0 until lines.size) {
            var currentMax = -1
            for (x in lines[0].length-1 downTo 0) {
                if (trees[x][y] <= currentMax) {
                    treeLiberties[x][y] --
                } else {
                    currentMax = trees[x][y]
                }
            }
        }

        for (x in 0 until lines[0].length) {
            var currentMax = -1
            for (y in 0 until lines.size) {
                if (trees[x][y] <= currentMax) {
                    treeLiberties[x][y] --
                } else {
                    currentMax = trees[x][y]
                }
            }
        }
        println(lines.size)
        println(lines[0].length)
        for (x in 0 until lines[0].length) {
            var currentMax = -1
            for (y in lines.size-1 downTo 0) {
                println(trees[x][y])
                if (trees[x][y] <= currentMax) {
                    treeLiberties[x][y] --
                } else {
                    currentMax = trees[x][y]
                }
            }
        }

        println(treeLiberties.fold(0) { r, it -> r + it.filter { it > 0 }.size })
    }

    fun part2() {
        val lines = Utils.getLines("8.txt")
        var trees = Array(lines.size) { y -> Array(lines[0].length) { x -> lines[y][x].digitToInt() } }.xToY()

        fun score(_x: Int, _y: Int): Int {
            val directions = arrayOf(
                Pair(0, 1),
                Pair(0, -1),
                Pair(1, 0),
                Pair(-1, 0)
            )
            val size = trees[_x][_y]
            var score = 1
            for (dir in directions) {
                var x = _x + dir.first
                var y = _y + dir.second
                var count = 1
                while (true) {
                    if (y !in trees.indices || x !in trees[0].indices) {
                        count--
                        break
                    }
                    if (trees[x][y] >= size)
                        break
                    count ++
                    x += dir.first
                    y += dir.second
                }
                score *= count
            }
            return score
        }

        var max = 0
        for (x in 0 until trees[0].size) {
            for (y in 0 until trees.size) {
                val s = score(x, y)
                max = if (s > max) s else max
            }
        }
        println(score(1, 2))

    }

}