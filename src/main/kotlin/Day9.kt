import kotlin.math.abs

object Day9 {

    fun part1() {
        val lines = Utils.getLines("9.txt")
        var H = Point(0, 0)
        val T = Point(0, 0)
        val visited = mutableListOf(Point(0, 0))
        val dirs = mapOf(
            Pair('U', Point(0, 1)),
            Pair('D', Point(0, -1)),
            Pair('L', Point(-1, 0)),
            Pair('R', Point(1, 0))
        )
        for (line in lines) {
            val dir = dirs[line[0]]!!
            val count = line.split(" ")[1].toInt()
            for (i in 0 until count) {
                H += dir
                val dx = H.x - T.x
                val dy = H.y - T.y
                if (abs(dx) > 2 || abs(dy) > 2)
                    println("dx $dx   dy: $dy")
                if (abs(dx) > 1 || abs(dy) > 1) {
                    if (abs(dx) > 1) {
                        T.x += dx/2
                        T.y = H.y
                    } else if (abs(dy) > 1) {
                        T.y += dy/2
                        T.x = H.x
                    }
                }
                if (!visited.any { it.x == T.x && it.y == T.y })
                    visited.add(T.clone())
            }
        }
        /*
        for (y in 0 until 200) {
            for (x in 0 until 200) {
                if (Point(x, y) in visited)
                    print("X ")
                else print(". ")
            }
            //println()
        }
         */
        println(visited.toList().size)
    }

    fun part2() {
        val lines = Utils.getLines("9.txt")
        val knots = Array(10) {
            Point(0, 0)
        }
        val visited = mutableListOf(Point(0, 0))
        val dirs = mapOf(
            Pair('U', Point(0, 1)),
            Pair('D', Point(0, -1)),
            Pair('L', Point(-1, 0)),
            Pair('R', Point(1, 0))
        )
        var c = 0
        for (line in lines) {
            val dir = dirs[line[0]]!!
            val count = line.split(" ")[1].toInt()
            for (i in 0 until count) {
                knots[0].x += dir.x
                knots[0].y += dir.y
                for (k in 1..9) {
                    val dx = knots[k-1].x - knots[k].x
                    val dy = knots[k-1].y - knots[k].y
                    if (abs(dx) > 2 || abs(dy) > 2)
                        println("dx $dx   dy: $dy")
                    if (abs(dx) > 1 || abs(dy) > 1) {
                        if (abs(dx) > abs(dy)) {
                            knots[k].x += dx - dx/abs(dx)
                            knots[k].y = knots[k-1].y
                        } else if (abs(dy) > abs(dx)) {
                            knots[k].y += dy - dy/abs(dy)
                            knots[k].x = knots[k-1].x
                        } else if(abs(dy) == abs(dx)) {
                            knots[k].y += dy - dy/abs(dy)
                            knots[k].x += dx - dx/abs(dx)
                        }
                    }
                    if (!visited.any { it.x == knots[k].x && it.y == knots[k].y } && k == 9)
                        visited.add(knots[k].clone())
                    /*
                    println("$k  :  $dx  :  $dy")
                    for (y in 14 downTo  -11) {
                        for (x in -5 .. 15) {
                            if (Point(x, y) in visited)
                                print("X ")
                            else if (Point(x, y) in knots) print("${knots.indexOf(Point(x, y))} ")
                            else print(". ")
                        }
                        println()
                    }
                    println("----------------------------")

                     */
                }

            }
            println(line)
            c++
        }
        for (y in 14 downTo -11) {
            for (x in -5 .. 15) {
                if (Point(x, y) in visited)
                    print("X ")
                else if (Point(x, y) in knots) print("${knots.indexOf(Point(x, y))} ")
                else print(". ")
            }
            println()
        }
        println(visited.toList().size)
    }

}