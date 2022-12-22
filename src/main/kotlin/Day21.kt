object Day21 {

    fun part1() {
        val input = Utils.getLines("21")
        var monkeys = input.map { Pair(it.split(": ")[0], it.split(": ")[1]) }.toMap().toMutableMap()
        val results = monkeys.filter { it.value.toLongOrNull() != null }.map { Pair(it.key, it.value.toLong()) }.toMap().toMutableMap()
        monkeys = monkeys.filter { it.value.toIntOrNull() == null } as MutableMap<String, String>

        while (monkeys.isNotEmpty()) {

            val mInterator = monkeys.iterator()
            while (mInterator.hasNext()) {
                val m = mInterator.next()
                val v1 = m.value.split(" ")[0]
                val v2 = m.value.split(" ")[2]

                if (v1 !in results || v2 !in results)
                    continue

                val n1 = results[v1]
                val n2 = results[v2]
                results[m.key] = when (m.value.split(" ")[1]) {
                    "+" -> n1!! + n2!!
                    "-" -> n1!! - n2!!
                    "*" -> n1!! * n2!!
                    "/" -> n1!! / n2!!
                    else -> 0
                }
                mInterator.remove()

                if (m.key == "root") {
                    println(results[m.key])
                    return
                }
            }

        }

    }


    fun part2() {
        val input = Utils.getLines("21")
        var monkeys = input.map { Pair(it.split(": ")[0], it.split(": ")[1]) }.toMap().toMutableMap()

        // calculate all "humn" dependent monkeys
        val deps = mutableListOf("humn")
        fun buildDeps(str: String): Boolean {
            val op = monkeys[str]!!
            if (op.toLongOrNull() != null)
                return false

            val n1 = monkeys[str]!!.split(" ")[0]
            val n2 = monkeys[str]!!.split(" ")[2]
            val res1 = buildDeps(n1)
            val res2 = buildDeps(n2)
            if (res1 || res2 || (n1 == "humn" || n2 == "humn"))
                deps.add(str)
            return res1 || res2 || (n1 == "humn" || n2 == "humn")
        }
        buildDeps("root")

        // calculate all independent values
        val results = monkeys.filter { it.value.toLongOrNull() != null }.map { Pair(it.key, it.value.toLong()) }.toMap().toMutableMap()
        monkeys = monkeys.filter { it.value.toIntOrNull() == null } as MutableMap<String, String>

        while (monkeys.filter { it.key !in deps }.isNotEmpty()) {

            val mInterator = monkeys.iterator()
            while (mInterator.hasNext()) {
                val m = mInterator.next()
                val v1 = m.value.split(" ")[0]
                val v2 = m.value.split(" ")[2]

                if (m.key in deps)
                    continue
                if (v1 !in results || v2 !in results)
                    continue

                val n1 = results[v1]
                val n2 = results[v2]
                results[m.key] = when (m.value.split(" ")[1]) {
                    "+" -> n1!! + n2!!
                    "-" -> n1!! - n2!!
                    "*" -> n1!! * n2!!
                    "/" -> n1!! / n2!!
                    else -> 0
                }
                mInterator.remove()
            }
        }

        // backtrack dependencies to find correct value for "humn"
        monkeys = input.map { Pair(it.split(": ")[0], it.split(": ")[1]) }.toMap().toMutableMap()
        var split = monkeys["root"]!!.split(" ")
        var curRes = results[when {
                split[0] !in deps -> split[0]
                split[2] !in deps -> split[2]
                else -> ""
            }]!!
        var next = when {
                split[0] in deps -> split[0]
                split[2] in deps -> split[2]
                else -> ""
            }

        while (true) {
            split = monkeys[next]!!.split(" ")
            var string = monkeys[next]!!
            next = when {
                split[0] in deps -> split[0]
                split[2] in deps -> split[2]
                else -> ""
            }
            val other = results[when {
                split[0] !in deps -> split[0]
                split[2] !in deps -> split[2]
                else -> 0
            }]!!
            string = string.replace(when {
                split[0] !in deps -> split[0]
                split[2] !in deps -> split[2]
                else -> ""
            }, other.toString())
            println("$string != $curRes")

            val op = split[1]
            val firstTarget = split[0] in deps
            curRes = when (op) {
                "+" -> curRes - other
                "-" -> if (firstTarget) curRes + other else other - curRes
                "*" -> curRes / other
                "/" -> if (firstTarget) curRes * other else other/curRes
                else -> 0
            }
            println("$next = $curRes")
            if (next == "humn") {
                println(curRes)
                break
            }
        }

    }

}