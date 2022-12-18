object Day2 {

    fun part1() {
        val lines = Utils.getLines("2_1.txt")
        var score = 0
        lines.forEach {
            if (!it.isEmpty()) {
                score += if (it[2] == 'X') 1 else if (it[2] == 'Y') 2 else if (it[2] == 'Z') 3 else 0
                when (it) {
                    in a("A Y", "B Z", "C X") ->  {
                        println("won")
                        score += 6
                    }
                    in a("A X", "B Y", "C Z") -> {
                        println("draw")
                        score += 3
                    }
                }
            }
        }
        println(score)
    }

    /*
    fun part1Smart() = println(Utils.getLines("2_1.txt").fold(0) { r, it ->
        it[2].digitToInt()-65 + if ()
    })

     */

    fun part2() {
        val lines = Utils.getLines("2_1.txt")

        val won = a(
            "A Y",
            "B Z",
            "C X"
        )
        val draw = a(
            "A X",
            "B Y",
            "C Z"
        )
        val lost = a(
            "A Z",
            "B X",
            "C Y"
        )

        lines.forEachIndexed {index, it ->
            if (!it.isEmpty()) {
                when (it[2]) {
                    'Y' -> { // draw
                        lines[index] = draw.filter { s-> s[0] == it[0] }[0]
                    }
                    'X' -> { // lost
                        lines[index] = lost.filter { s-> s[0] == it[0] }[0]
                    }
                    'Z' -> {
                        lines[index] = won.filter { s-> s[0] == it[0] }[0]
                    }
                }
            }
        }

        var score = 0
        lines.forEach {
            if (!it.isEmpty()) {
                score += if (it[2] == 'X') 1 else if (it[2] == 'Y') 2 else if (it[2] == 'Z') 3 else 0
                when (it) {
                    in a("A Y", "B Z", "C X") ->  {
                        score += 6
                    }
                    in a("A X", "B Y", "C Z") -> {
                        score += 3
                    }
                }
            }
        }
        println(score)
    }

}