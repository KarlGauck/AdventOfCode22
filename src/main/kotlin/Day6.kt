object Day6 {

    fun part1() {
        val line = Utils.getLines("6.txt")[0]
        for (i in 3 until line.length) {
            if (setOf(line[i], line[i-1], line[i-2], line[i-3]).size == 4)
                println(i+1)
        }
    }

    fun part2() {
        val line = Utils.getLines("6.txt")[0]
        for (i in 13 until line.length) {
            val set = mutableSetOf<Char>()
            for (x in 0..13)
                set.add(line[i-x])
            if (set.size == 14)
                println(i+1)
        }
    }
}