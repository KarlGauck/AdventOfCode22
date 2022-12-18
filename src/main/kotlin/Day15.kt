import kotlin.math.abs

object Day15 {

    fun input(src: String) = Utils.getLines(src).map { listOf(
        it.split(": closest")[0].split("at ")[1].convert { Point(it.split(", y=")[0].replace("x=", "").toInt(), it.split(", y=")[1].toInt()) },
        it.split(": closest")[1].split("at ")[1].convert { Point(it.split(", y=")[0].replace("x=", "").toInt(), it.split(", y=")[1].toInt()) } )
    }

    fun biginput(src: String) = Utils.getLines(src).map { listOf(
        it.split(": closest")[0].split("at ")[1].convert { a(it.split(", y=")[0].replace("x=", "").toLong(), it.split(", y=")[1].toLong()) },
        it.split(": closest")[1].split("at ")[1].convert { a(it.split(", y=")[0].replace("x=", "").toLong(), it.split(", y=")[1].toLong()) } )
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
        val scanners = biginput("15")

        var points = mutableListOf<Array<Long>>()
        var scannerNum = 0
        for (s in scanners) {
            scannerNum ++
            println("scanner: $scannerNum")
            val dist = abs(s[0][0]-s[1][0])+abs(s[0][1]-s[1][1])
            for (x in s[0][0]-dist-1..s[0][0]+dist+1) {
                val p1 = a(x, s[0][1] + (dist+1-abs(s[0][0]-x)))
                val p2 = a(x, s[0][0] - (dist+1-abs(s[0][0]-x)))
                if (!(p1[0]<0 || p1[0]>lim || p1[1]<0 || p1[1]>lim))
                    points+=p1
                if (!(p2[0]<0 || p2[0]>lim || p2[1]<0 || p2[1]>lim))
                    points+=p2
            }
            p@for (p in points) {
                for (s in scanners) {
                    val pdist = abs(s[0][0]-p[0])+abs(s[0][1]-p[1])
                    val sdist = abs(s[0][0]-s[1][0])+abs(s[0][1]-s[1][1])
                    if (pdist<=sdist) {
                        continue@p
                    }
                }
                println("${p[0]} ${p[1]}")
                println(p[0]*4_000_000+p[1])
                return
            }
            points = mutableListOf()
        }

    }

}