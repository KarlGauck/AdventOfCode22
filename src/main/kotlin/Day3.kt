object Day3 {

    fun part1() {
        val chars = Utils.getLines("3.txt").map { str -> str.split(str.length/2-1).map { it.toSet() }.fold(
            str.toSet()
        ) { set, it -> set.intersect(it) }.toList()[0] }

        var count = 0
        for (c in chars) {
            count += if (c.isLowerCase())
                c.toInt()-96
            else
                c.toInt()-38
        }
        println(count)
    }

    fun part2() {
        val chars = Utils.getLines("3.txt").chunked(3).map { it.fold(it[0].toSet()) { set, it -> set.intersect(it.toSet()) }.toList()[0] }

        var count = 0
        for (c in chars) {
            count += if (c.isLowerCase())
                c.toInt()-96
            else
                c.toInt()-38
        }
        println(count)
    }
}