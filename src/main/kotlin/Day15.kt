import kotlin.math.abs

object Day15 {

    fun input(src: String) = Utils.getLines(src).map { listOf(
        it.split(": closest")[0].split("at ")[1].convert { Point(it.split(", y=")[0].replace("x=", "").toInt(), it.split(", y=")[1].toInt()) },
        it.split(": closest")[1].split("at ")[1].convert { Point(it.split(", y=")[0].replace("x=", "").toInt(), it.split(", y=")[1].toInt()) } )
    }
    fun part1() {
        val scanners = input("15")
        val maxX = scanners.maxOf { it -> it.maxOf { it.x } }
        val minX = scanners.minOf { it -> it.minOf { it.x } }
        val maxDist = scanners.maxOf { abs(it[0].x - it[1].x) + abs(it[0].y - it[1].y) }
        val distances = scanners.associate { Pair(it[0], abs(it[0].x - it[1].x) + abs(it[0].y - it[1].y)) }.toMutableMap()
        val beacons = scanners.map { it[1] }

        var sum = 0
        val y = 2000000
        for (x in minX-maxDist..maxX+maxDist) {
            if (Point(x, y) in beacons)
                continue
            for (s in scanners) {
                val dist = abs(s[0].x-x) + abs(s[0].y-y)
                if (dist <= distances[s[0]]!!) {
                    sum++
                    break
                }
            }
        }
        println(sum)
    }

    fun part2() {
        val lim = 4_000_000
        val scanners = input("15")
        val distances = scanners.associate { Pair(it[0], abs(it[0].x - it[1].x) + abs(it[0].y - it[1].y)) }.toMutableMap()

        var points = mutableSetOf<Point>()
        var scannerNum = 0
        for (s in scanners) {
            scannerNum ++
            println("scanner: $scannerNum")
            val dist = abs(s[0].x-s[1].x)+abs(s[0].y-s[1].y)
            for (x in s[0].x-dist-1..s[0].x+dist+1) {
                points+=Point(s[0].y + (dist+1-abs(s[0].x-x)), x)
                points+=Point(s[0].y - (dist+1-abs(s[0].x-x)), x)
            }
            p@for (p in points) {
                if (p.x !in 0..lim || p.y !in 0..lim)
                    continue
                for (s in scanners) {
                    if (distances[s[0]]!! <0)
                        println("HEEEEEEEEELP")
                    if (abs(s[0].x-p.x)+abs(s[0].y-p.y)<=distances[s[0]]!!)
                        continue@p
                }
                println("${p.x} ${p.y}")
                return
            }
            points = mutableSetOf()
        }

    }

}