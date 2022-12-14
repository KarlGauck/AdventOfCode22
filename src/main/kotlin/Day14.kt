import kotlin.math.abs

object Day14 {

    fun input(src: String): MutableList<Point> {
        val lines = Utils.getLines(src).map { it.split(" -> ").map { Point(it.split(",")[0].toInt(), it.split(",")[1].toInt()) } }
        val points = mutableListOf<Point>()
        for (line in lines) {
            for (pI in 1 until line.size) {
                var p0 = line[pI-1]
                val p1 = line[pI]
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
        val points = input("14t")
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
        val points = input("14t")
        var sum = 1
        val floorY = points.maxOf { it.y } + 2
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