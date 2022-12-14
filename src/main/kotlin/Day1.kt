object Day1 {

    fun part1() {
        var lines = Utils.getLines("1_1.txt")

        var max = -1
        var sum = 0

        for (line in lines) {
            if (line == "") {
                if (sum > max)
                    max = sum
                sum = 0
                continue
            }
            sum += line.toInt()
        }

        println(max)
    }

    fun part1Smart() = println(Utils.getLines("1_1.txt").split("").maxOfOrNull { it.fold(0) { r, n -> r + n.toInt() } })

    fun part2() {
        var lines = Utils.getLines("1_1.txt")

        var max = mutableListOf(0, 0, 0)
        var sum = 0

        for (line in lines) {
            if (line == "") {
                if (sum > max.min()) {
                    max.remove(max.min())
                    max.add(sum)
                }
                sum = 0
                continue
            }
            sum += line.toInt()
        }

        println(max.fold(0)  {it, new -> it + new})
    }

    fun part2Smart() = println(Utils.getLines("1_1.txt").split("").map { it -> it.sumOf { it.toInt() } }.sortedDescending().subList(0, 3).sum())

}