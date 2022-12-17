import java.lang.Integer.max

object Day17 {

    val input = Utils.getLines("17")[0].map { when (it) {
        '<' -> -1
        '>' -> 1
        else -> 0
    } }.toMutableList()

    var pushed = 0
    val push: Int
        get() {
            val res = input.removeAt(0)
            input.add(res)
            pushed ++
            pushed %= input.size
            return res
        }

    val shapes = listOf(
            listOf(intArrayOf(0, 0), intArrayOf(1, 0), intArrayOf(2, 0), intArrayOf(3, 0)),
            listOf(intArrayOf(0, 1), intArrayOf(1, 0), intArrayOf(1, 1), intArrayOf(1, 2), intArrayOf(2, 1)),
            listOf(intArrayOf(0, 0), intArrayOf(1, 0), intArrayOf(2, 0), intArrayOf(2, 1), intArrayOf(2, 2)),
            listOf(intArrayOf(0, 0), intArrayOf(0, 1), intArrayOf(0, 2), intArrayOf(0, 3)),
            listOf(intArrayOf(0, 0), intArrayOf(0, 1), intArrayOf(1, 0), intArrayOf(1, 1))
        )

    fun part1() {

        val space = Array(7) {
            Array(10000) {false}
        }

        var highestRock = -1
        for (i in 0 until 2022) {
            val shape = shapes[i % 5]
            val pos = arrayOf(2, highestRock+4)
            while(true) {
                val push = push
                if (shape.all {
                        it[0]+pos[0]+push in space.indices && !space[it[0]+pos[0]+push][it[1]+pos[1]]
                    }) pos[0] += push
                if (shape.all {
                        it[1]+pos[1]-1 in space[0].indices && !space[it[0]+pos[0]][it[1]+pos[1]-1]
                    }) {
                    pos[1]--
                }
                else break
            }
            for (it in shape)
                space[it[0]+pos[0]][it[1]+pos[1]] = true
            highestRock = space.maxOf { it -> it.indexOfLast { it } }
        }
        println(highestRock+1)
    }

    fun part2() {

        val space = Array(7) {
            Array(2000) {false}
        }

        val states = mutableMapOf<Triple<Int, Int, List<Boolean>>, Pair<Long, Long>>()

        var height = 0L
        var highestRock = -1
        var i = 0L
        val end = 1000000000000
        while (i < end) {
            val shape = shapes[i.mod(5)]
            val pos = arrayOf(2, highestRock+4)
            while(true) {
                val push = push
                if (shape.all {
                        it[0]+pos[0]+push in space.indices && !space[it[0]+pos[0]+push][it[1]+pos[1]]
                    }) pos[0] += push
                if (shape.all {
                        it[1]+pos[1]-1 in space[0].indices && !space[it[0]+pos[0]][it[1]+pos[1]-1]
                    }) {
                    pos[1]--
                }
                else break
            }

            for (it in shape)
                space[it[0]+pos[0]][it[1]+pos[1]] = true
            if (highestRock > 1000) {
                var minRock = space.minOf { it -> it.indexOfLast { it } }
                for (x in space.indices)
                    space[x].shiftDown(minRock+1, false)
                height += minRock+1
            }
            highestRock = space.maxOf { it -> it.indexOfLast { it } }

            val state = Triple(i.mod(5), pushed, (0 until 7).fold(mutableListOf<Boolean>()) {r, it -> r.plus(space[it][0]).toMutableList()})
            if (state in states.keys) {
                val dheight = height+highestRock-states[state]!!.second
                val di = i-states[state]!!.first
                val reps = (end-i) / di
                height += reps*dheight
                i += reps*di
            }
            else
                states[state] = Pair(i, height+highestRock)
            i++
        }
        println(height+highestRock+1)

    }

}