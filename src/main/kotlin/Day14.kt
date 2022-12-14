import kotlin.math.abs

object Day14 {

    fun input(src: String): MutableList<Point> {
        val lines = Utils.getLines(src).map { it.split(" -> ").map { it.split(",") } }
        val points = mutableListOf<Point>()
        for (line in lines) {
            for (pI in 1 until line.size) {
                var p0 = Point(line[pI-1][0].toInt(), line[pI-1][1].toInt())
                val p1 = Point(line[pI][0].toInt(), line[pI][1].toInt())
                val dir = (p1-p0)/ abs(p1.x-p0.x + p1.y-p0.y)
                points.add(p0.clone())
                while (p0 != p1) {
                    p0 += dir
                    points.add(p0.clone())
                }
            }
        }
        return points
    }

    fun part1() {
        val points = input("14")
        var grounded = true
        var sum = 0
        while (grounded) {
            var sand = Point(500, 0)
            while(true) {
                if (sand.y > points.maxOf { it.y }) {
                    grounded = false
                    break
                }
                if (sand + Point(0, 1) !in points)
                    sand += Point(0, 1)
                else if (sand + Point(-1, 1) !in points)
                    sand += Point(-1, 1)
                else if (sand + Point(1, 1) !in points)
                    sand += Point(1, 1)
                else {
                    points.add(sand)
                    sum++
                    break
                }
            }
        }
        println(sum)
    }

    fun part2() {
        val points = input("14")
        var sum = 1
        val floorY = points.maxOf { it.y } + 2
        println("${points.minOf { it.x }} ${points.maxOf { it.x }} ${points.minOf { it.y }} ${points.maxOf { it.y }}")
        var sand = mutableSetOf(Point(500, 0))
        while(sand.isNotEmpty()) {
            val newSand = mutableSetOf<Point>()
            for (s in sand) {
                if (s + Point(0, 1) !in points && s.y+1 < floorY)
                    newSand += s + Point(0, 1)
                if (s + Point(-1, 1) !in points && s.y+1 < floorY)
                    newSand += s + Point(-1, 1)
                if (s + Point(1, 1) !in points && s.y+1 < floorY)
                    newSand += s + Point(1, 1)
            }
            sum += newSand.size
            sand = newSand
        }
        println(sum)
    }

}