object Day4 {

    fun part1() = println(Utils.getLines("4.txt").fold(0) { r, it ->
        var r1 = it.split(",")[0].split("-")
        var range1 = (r1[0].toInt()..r1[1].toInt()).toSet()
        val r2 = it.split(",")[1].split("-")
        val range2 = (r2[0].toInt()..r2[1].toInt()).toSet()
        if (range1.containsAll(range2) || range2.containsAll(range1)) r+1 else r
    })

    fun part2() = println(Utils.getLines("4.txt").fold(0) { r, it ->
        var r1 = it.split(",")[0].split("-")
        var range1 = (r1[0].toInt()..r1[1].toInt()).toSet()
        val r2 = it.split(",")[1].split("-")
        val range2 = (r2[0].toInt()..r2[1].toInt()).toSet()
        if (!range1.intersect(range2).isEmpty() || !range2.intersect(range1).isEmpty()) r+1 else r
    })
}