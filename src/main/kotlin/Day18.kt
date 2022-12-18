import kotlin.math.abs

object Day18 {

    fun part1() {
        val cubes = Utils.getLines("18").map { it.split(",").map { it.toInt() }.toTypedArray() }
        val neighbours = mutableListOf<Pair<Array<Int>,Array<Int>>>()
        for (cube in cubes) {
            for (dx in -1..1)
                for (dy in -1..1)
                    for (dz in -1..1) {
                        if (abs(dx)+abs(dy)+ abs(dz) != 1)
                            continue
                        val nP = a(cube[0]+dx,cube[1]+dy,cube[2]+dz)
                        if (neighbours.any { it.first.contentEquals(cube) && it.second.contentEquals(nP) } || neighbours.any { it.first.contentEquals(
                                nP
                            ) && it.second.contentEquals(cube)
                            }) {
                            println("continue")
                            continue
                        }
                        neighbours.add(Pair(cube, nP))
                    }
        }
        println(neighbours.size)
        println(cubes.size*6)
        println(-6*cubes.size + 2*neighbours.size)
    }

    fun part2() {
        val cubes = Utils.getLines("18").map { it -> it.split(",").map { it.toInt() }.toTypedArray() }
        infix fun List<Array<Int>>.has(e: Array<Int>): Boolean = this.any {
            it[0]==e[0]&&it[1]==e[1]&&it[2]==e[2]
        }



        val checked = mutableListOf<Array<Int>>()
        val inside = mutableListOf<Array<Int>>()
        fun checkInside(cube: Array<Int>)  {
            if (cubes has cube)
                return
            val xRange = cubes.minOf { it[0] }..cubes.maxOf { it[0] }
            val yRange = cubes.minOf { it[1] }..cubes.maxOf { it[1] }
            val zRange = cubes.minOf { it[2] }..cubes.maxOf { it[2] }
            val fluid = mutableListOf(cube)
            var flow = true
            var isInside = true
            while(flow) {
                flow = false
                for (cube in fluid.map { it }) {
                    forDeltaXYZ(1, 1, 1, fun(dx: Int, dy: Int, dz: Int): Boolean {
                        if (abs(dx)+abs(dy)+abs(dz) != 1)
                            return false
                        val nC = a(cube[0]+dx, cube[1]+dy, cube[2]+dz)
                        checked.add(nC)
                        if (fluid has nC)
                            return false
                        if (cubes has nC)
                            return false
                        if (nC[0] !in xRange || nC[1] !in yRange || nC[2] !in zRange) {
                            isInside = false
                            return true
                        }
                        flow = true
                        fluid.add(nC)
                        return false
                    })
                }
            }
            if (!isInside)
                return
            println(cube.display())
            inside.addAll(fluid)
        }


        val neighbours = mutableListOf<Pair<Array<Int>,Array<Int>>>()
        for (cube in cubes) {
            forDeltaXYZ(1, 1, 1, fun(dx: Int, dy: Int, dz: Int): Boolean {
                if (abs(dx)+abs(dy)+ abs(dz) != 1)
                    return false
                val nC = a(cube[0]+dx,cube[1]+dy,cube[2]+dz)
                if (!(checked has nC)) {
                    checkInside(nC)
                    checked.add(nC)
                }
                if (neighbours.any { it.first.contentEquals(cube) && it.second.contentEquals(nC) } ||
                    neighbours.any { it.first.contentEquals(nC) && it.second.contentEquals(cube) })
                {
                    return false
                }
                neighbours.add(Pair(cube, nC))
                return false
            })
        }

        val insideneighbours = mutableListOf<Pair<Array<Int>,Array<Int>>>()
        for (cube in inside) {
            forDeltaXYZ(1, 1, 1, fun(dx: Int, dy: Int, dz: Int): Boolean {
                if (abs(dx)+abs(dy)+ abs(dz) != 1)
                    return false
                val nC = a(cube[0]+dx,cube[1]+dy,cube[2]+dz)
                if (insideneighbours.any { it.first.contentEquals(cube) && it.second.contentEquals(nC) } ||
                    insideneighbours.any { it.first.contentEquals(nC) && it.second.contentEquals(cube) })
                {
                    return false
                }
                insideneighbours.add(Pair(cube, nC))
                return false
            })
        }

        println(-6*cubes.size + 2*neighbours.size +6*inside.size - 2*insideneighbours.size)
    }

}