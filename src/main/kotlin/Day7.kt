import java.util.Stack

object Day7 {

    fun part1() {
        val lines = Utils.getLines("7.txt")

        val dirs = mutableMapOf<String, MutableSet<String>>(
            Pair("/", mutableSetOf())
        )
        val dirStack = Stack<String>()
        dirStack.add("/")

        for (line in lines) {
            when {
                "dir" in line -> {
                    val dir = line.split(" ")[1]
                    if (!dirs.containsKey(dir))
                        dirs["${dirStack.peek()}/$dir"] = mutableSetOf()
                    dirs[dirStack.peek()]?.add("dir:${dirStack.peek()}/$dir")
                }
                "$ cd " in line -> {
                    val dir = line.split(" ")[2]
                    if (".." in dir)
                        dirStack.pop()
                    else
                        dirStack.push("${dirStack.peek()}/$dir")
                }
                "$ ls" in line -> continue
                else -> {
                    val fileSize = line.split(" ")[0].toInt()
                    val file = line.split(" ")[1]
                    dirs[dirStack.peek()]?.add("$file:$fileSize")
                }
            }
        }

        var dirSizes = mutableMapOf<String, Int>()

        fun dirSum(dir: String, d: Int = 0): Int {
            println(d)
            if (dirSizes.containsKey(dir))
                return dirSizes.get(dir)!!
            else {
                val content = dirs[dir]
                var sum = 0
                for (c in content!!.toList()) {
                    when {
                        "dir" in c -> {
                            val name = c.replace("dir:", "")
                            sum += dirSum(name, d+1)
                        }
                        else -> {
                            val fileName = c.split(":")[0]
                            sum += c.split(":")[1].toInt()
                        }
                    }
                }
                dirSizes.put(dir, sum)
                return sum
            }
        }

        for (dir in dirs) {
            println(dir)
        }


        var sum = 0
        for (dir in dirs) {
            val size = dirSum(dir.key)
            if (size <= 100000)
                sum += size
        }
        println(sum)
    }

    fun part2() {
        val lines = Utils.getLines("7.txt")

        val dirs = mutableMapOf<String, MutableSet<String>>(
            Pair("/", mutableSetOf())
        )
        val dirStack = Stack<String>()
        dirStack.add("/")

        for (line in lines) {
            when {
                "dir" in line -> {
                    val dir = line.split(" ")[1]
                    if (!dirs.containsKey(dir))
                        dirs["${dirStack.peek()}/$dir"] = mutableSetOf()
                    dirs[dirStack.peek()]?.add("dir:${dirStack.peek()}/$dir")
                }
                "$ cd " in line -> {
                    val dir = line.split(" ")[2]
                    if (".." in dir)
                        dirStack.pop()
                    else
                        dirStack.push("${dirStack.peek()}/$dir")
                }
                "$ ls" in line -> continue
                else -> {
                    val fileSize = line.split(" ")[0].toInt()
                    val file = line.split(" ")[1]
                    dirs[dirStack.peek()]?.add("$file:$fileSize")
                }
            }
        }

        var dirSizes = mutableMapOf<String, Int>()
        var total = 0

        fun dirSum(dir: String, d: Int = 0): Int {
            if (dirSizes.containsKey(dir))
                return dirSizes.get(dir)!!
            else {
                val content = dirs[dir]
                var sum = 0
                for (c in content!!.toList()) {
                    when {
                        "dir" in c -> {
                            val name = c.replace("dir:", "")
                            val newSum = dirSum(name, d+1)
                            sum += newSum
                        }
                        else -> {
                            c.split(":")[0]
                            val newSum = c.split(":")[1].toInt()
                            sum += newSum
                        }
                    }
                }
                if (d == 0)
                    total += sum
                dirSizes.put(dir, sum)
                return sum
            }
        }

        for (dir in dirs)
           dirSum(dir.key)
        val free = (70_000_000 -total)
        val necessary = 30000000 - (70_000_000 - total)
        println(dirSizes.filter { it.value >= necessary }.minOf { it.value })


    }

}