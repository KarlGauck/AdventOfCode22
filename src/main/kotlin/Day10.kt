object Day10 {

    fun part1() {
        val lines = Utils.getLines("10.txt")
        var cycles = 0
        var x = 1
        var sum = 0

        for (line in lines) {
            when (line){
                "noop" -> {
                    cycles++
                    if (cycles in Array(30) {20 + it  * 40}) {
                        sum += cycles*x
                        println("$cycles $x ${cycles*x}")
                    }
                }
                else -> {
                    val dx = line.split(" ")[1].toInt()
                    cycles++
                    if (cycles in Array(40) {20 + it  * 40}) {
                        println("$cycles $x ${cycles*x}")
                        sum += cycles*x
                    }
                    cycles++
                    if (cycles in Array(40) {20 + it  * 40}) {
                        println("$cycles $x ${cycles*x}")
                        sum += cycles*x
                    }
                    x += dx
                }
            }
        }
        println(sum)
    }

    fun part2() {
        val lines = Utils.getLines("10.txt")
        var cycles = 0
        var x = 1
        var sum = 0
        var h = -1

        var pixels = Array(40) {Array(6) {false} }

        for (line in lines) {

            fun add(cy: Int, xv: Int) {
                for (i in 0 until cy) {
                    if (cycles % 40 == 0)
                        h++
                    cycles++
                    var nx = cycles
                    while (nx > 40)
                        nx -= 40
                    nx --
                    if (nx in pixels.indices && nx in a(xv-1, xv, xv + 1)) {
                        pixels[nx][h] = true
                    }
                    for (u in 0..39) {
                        if (pixels[u][h])
                            print("█")
                        else
                            print(" ")
                    }
                    println("--- $cycles")
                }
            }

            when (line){
                "noop" -> {
                    add(1, x)
                }
                else -> {
                    val dx = line.split(" ")[1].toInt()
                    add(2, x)
                    x += dx
                }
            }
        }

        for (y in pixels.xToY()) {
            for (x in y) {
                if (x)
                    print("█ ")
                else
                    print("  ")
            }
            println()
        }
        println(sum)
        // RLEZFLGE
    }

}