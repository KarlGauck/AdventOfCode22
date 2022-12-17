import java.lang.Integer.max
import kotlin.math.min

object Day16 {

    private val valves = mutableListOf<String>()
    private val flow = mutableMapOf<String, Int>()
    val next = mutableMapOf<String, MutableList<String>>()

    private fun input(src: String) {
        val lines = Utils.getLines(src)
        for (l in lines) {
            val valve = l.split(" has")[0].replace("Valve ","")
            valves += valve
            flow[valve] = l.split(";")[0].split("=")[1].toInt()
            next[valve] = l.split("valve")[1].replace(" ", "").replace("s", "").split(",") as MutableList<String>
        }
    }

    fun distances(node: String): MutableMap<String, Int> {
        val distances = mutableMapOf(Pair(node, 0))
        val visited = mutableListOf<String>()
        val queue = mutableListOf(node)

        while (queue.isNotEmpty()) {
            val node = queue.removeAt(0)
            for (n in next[node]!!) {
                if (n !in visited && n !in queue) {
                    distances[n] = distances[node]!!+1
                    queue.add(n)
                }
            }
            visited.add(node)
        }
        return distances
    }

    private fun singlepaths(nodeSubset: List<String>): MutableList<Path> {
        val finishedPaths = mutableListOf<Path>()
        var paths = mutableListOf(Path("AA"))
        var maxPressure = 0
        while (paths.isNotEmpty()) {
            val newPaths = mutableListOf<Path>()
            for (path in paths) {

                val distances = distances(path.current)

                var totalPossible = path.value
                for (node in valves.filter { it !in path.open }) {
                    totalPossible += path.minute*flow[node]!!
                }
                if (totalPossible < maxPressure)
                    continue

                for (pair in distances.filter { it.key !in path.open && it.key in nodeSubset}) {
                    val value = (path.minute-pair.value-1)*flow[pair.key]!!

                    val newPath = path.copy()
                    newPath.current = pair.key
                    newPath.value = path.value + value
                    newPath.minute = path.minute - pair.value-1
                    newPath.open.add(newPath.current)
                    if (newPath.minute > 0)
                        newPaths.add(newPath)
                }
                finishedPaths.add(path)
            }
            paths = newPaths
            if (paths.isNotEmpty())
                maxPressure = paths.maxOf { it.value }
        }
        return finishedPaths
    }

    private fun doublepaths(nodeSubset: List<String>): MutableList<Pair<Path, Path>> {
        val finishedPaths = mutableListOf<Pair<Path, Path>>()
        var paths = mutableListOf(Pair(Path("AA", 26), Path("AA", 26)))
        var maxPressure = 0
        while (paths.isNotEmpty()) {
            val newPaths = mutableListOf<Pair<Path, Path>>()
            for (pathPair in paths) {
                var path: Path
                var other: Path
                if (pathPair.first.target == null) {
                    path = pathPair.first
                    other = pathPair.second
                }
                else if (pathPair.second.target == null) {
                    path = pathPair.second
                    other = pathPair.first
                }
                else {
                    path = pathPair.first
                    other = pathPair.second
                }

                val distances = distances(path.current)
                val otherdistances = distances(other.current)

                var totalPossible = path.value + other.value
                for (node in valves.filter { it !in path.open && it !in other.open }) {
                    if (distances[node]!! <= otherdistances[node]!!)
                        totalPossible += (path.minute)*flow[node]!!
                    else
                        totalPossible += (other.minute)*flow[node]!!
                }
                if (totalPossible < maxPressure)
                    continue

                for (pair in distances.filter { it.key !in path.open && it.key in nodeSubset && it.key !in other.open && (other.target==null || it.key != other.target!!.first) }) {
                    val value = (path.minute-pair.value-1)*flow[pair.key]!!

                    val newPath = path.copy()
                    newPath.current = pair.key
                    newPath.value = path.value + value
                    newPath.minute = path.minute - pair.value-1
                    newPath.open.add(newPath.current)


                    if (other.target != null) {
                        if (otherdistances[newPath.current]!! < pair.value)
                            continue
                    }

                    val newOther = other.copy()
                    if (newOther.target != null && newOther.target!!.second == newPath.minute)
                        newPath.target = null
                    else
                        newPath.target = Pair(pair.key, newPath.minute)
                    if (newOther.target != null && newOther.target!!.second >= newPath.minute)
                        newOther.target = null

                    if (newPath.minute > 0)
                        newPaths.add(Pair(newPath, newOther))
                }
                finishedPaths.add(pathPair)
            }
            paths = newPaths
            if (paths.isNotEmpty())
                maxPressure = max(paths.maxOf { it.first.value + it.second.value }, finishedPaths.maxOf { it.first.value + it.second.value })
        }
        return finishedPaths
    }

    fun part1() {
        input("16")
        println(singlepaths(valves.filter { flow[it]!! != 0 }).maxOf { it.value })
    }

    fun part2() {
        input("16benni")
        val res = doublepaths(valves.filter { flow[it]!! != 0 }).maxBy { it.first.value + it.second.value }
        println(res.first.value+res.second.value)
        // not 2952
    }

    class Path (var current: String, var minute: Int = 30){
        var open = mutableListOf<String>()
        var value = 0
        var target: Pair<String, Int>? = null

        fun copy(): Path {
            val p = Path(current)
            p.minute = minute
            p.open = open.map { it }.toMutableList()
            p.value = value
            return p
        }
    }

}