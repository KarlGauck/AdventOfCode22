import java.util.*

object Day23 {

    enum class Dir(val move: Pos) {
        N(a(0, -1)),
        NE(a(1, -1)),
        E(a(1, 0)),
        SE(a(1, 1)),
        S(a(0, 1)),
        SW(a(-1, 1)),
        W(a(-1, 0)),
        NW(a(-1, -1));

        fun important() = when(this) {
            N -> listOf(N, NE, NW)
            E -> listOf(E, NE, SE)
            W -> listOf(W, NW, SW)
            S -> listOf(S, SW, SE)
            else -> listOf()
        }

        companion object {
            fun strongDirs(i: Int): List<Dir> {
                val list = listOf(N, S, W, E)
                Collections.rotate(list, -i)
                return list
            }
        }
    }

    fun part1() {
        val lines = Utils.getLines("23")
        val elves = mutableListOf<Pos>()
        for (y in lines.indices)
            for (x in lines[y].indices) {
                if (lines[y][x] == '#')
                    elves += a(x, y)
            }

        for (i in 0 until 10) {

            println("next")
            val proposedDest: Array<Pos?> = elves.map { null }.toTypedArray()

            val strongDir = Dir.strongDirs(i)
            // first half of round: elves are proposing destinations
            for (elfWI in elves.withIndex()) {
                val elf = elfWI.value
                val elfI = elfWI.index

                if (Dir.values().none { d -> elves.any { it.contentEquals(elf vectorPlus d.move) } })
                    continue

                for (dir in strongDir) {
                    if (dir.important().any { d -> elves.any { it.contentEquals((elf vectorPlus d.move)) } })
                        continue
                    proposedDest[elfI] = (elf vectorPlus dir.move)!!
                    break
                }
            }

            // second half of round: elves move to their proposed destination if they were the only one to propose that destination
            for (elfWI in elves.withIndex()) {
                val elfI = elfWI.index
                val elf = elfWI.value
                if (proposedDest[elfI] == null)
                    continue
                if (proposedDest.count { it.contentEquals(proposedDest[elfI]) } <= 1)
                    elf.xy = proposedDest[elfI]!!
            }

        }

        val maxX = elves.maxOf { it.x }
        val minX = elves.minOf { it.x }
        val maxY = elves.maxOf { it.y }
        val minY = elves.minOf { it.y }
        val rectSize = (maxX-minX+1)*(maxY-minY+1)
        println(rectSize-elves.size)
    }

    fun part2() {
        val lines = Utils.getLines("23")
        val elves = mutableListOf<Pos>()
        for (y in lines.indices)
            for (x in lines[y].indices) {
                if (lines[y][x] == '#')
                    elves += a(x, y)
            }

        var i = 0
        var maxX = elves.maxOf { it.x }
        var minX = elves.minOf { it.x }
        var maxY = elves.maxOf { it.y }
        var minY = elves.minOf { it.y }
        var grid = Array(maxX-minX+1) { Array(maxY-minY+1) { false } }

        for (elf in elves) {
            grid[elf.x - minX][elf.y - minY] = true
        }

        for (y in grid[0].indices) {
                for (x in grid.indices)
                    print(if(grid[x][y]) "#" else ".")
                println()
            }

        while (true) {

            println("next")
            val proposedDest: Array<Pos?> = elves.map { null }.toTypedArray()
            var moved = false

            // first half of round: elves are proposing destinations
            val strongDir = Dir.strongDirs(i)
            for (elfWI in elves.withIndex()) {
                val elf = elfWI.value
                val elfI = elfWI.index

                if (Dir.values().none { d ->
                        val npos = ((elf vectorPlus d.move)!! vectorPlus a(-minX, -minY))!!
                        npos in grid && grid[npos] })
                    continue

                for (dir in strongDir) {

                    if (dir.important().any { d ->
                        val npos = ((elf vectorPlus d.move)!! vectorPlus a(-minX, -minY))!!
                        npos in grid && grid[npos] }) {
                        continue
                    }
                    val propPos = (elf vectorPlus dir.move)!!
                    proposedDest[elfI] = propPos
                    break
                }
            }

            // second half of round: elves move to their proposed destination if they were the only one to propose that destination
            for (y in grid[0].indices) {
                for (x in grid.indices)
                    print(if(grid[x][y]) "#" else ".")
                println()
            }
            println(elves.any { e -> elves.count { e.contentEquals(it) } > 1 })

            for (elfWI in elves.withIndex()) {
                val elfI = elfWI.index
                val elf = elfWI.value
                if (proposedDest[elfI] == null)
                    continue
                if (proposedDest.count { it.contentEquals(proposedDest[elfI]) } <= 1) {
                    moved = true
                    elf.xy = proposedDest[elfI]!!
                }
            }

            println(elves.any { e -> elves.count { e.contentEquals(it) } > 1 })

            maxX = elves.maxOf { it.x }
            minX = elves.minOf { it.x }
            maxY = elves.maxOf { it.y }
            minY = elves.minOf { it.y }
            grid = Array(maxX-minX+1) { Array(maxY-minY+1) { false } }
            for (elf in elves) grid[elf.x - minX][elf.y - minY] = true

            for (y in grid[0].indices) {
                for (x in grid.indices)
                    print(if(grid[x][y]) "#" else ".")
                println()
            }


            if (!moved) {
                println(i+1)
                return
            }

            i++
        }
    }
}
