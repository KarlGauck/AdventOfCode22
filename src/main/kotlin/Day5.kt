import java.util.Stack

object Day5 {

    fun part1() {
        var lines = Utils.getLines("5.txt")
        val stacks = MutableList(9) {
            Stack<Char>()
        }
        for (i in 7 downTo 0) {
            var stackId = 0
            for (x in 1..100 step 4) {
                if (stackId > 8)
                    break
                stacks[stackId].push(lines[i][x])
                stackId ++
            }
        }

        for (stack in stacks)
            stack.removeIf { it == ' ' }

        lines = lines.subList(10, lines.size)
        for (line in lines) {
            val iterCount = line.split("from")[0].replace("move", "").replace(" ", "").toInt()
            val startIndex = line.split("from")[1].replace(" ", "")[0].digitToInt()-1
            val endIndex = line.split("to")[1].replace(" ", "").last().digitToInt()-1
            println("$iterCount $startIndex $endIndex")
            for (interaction in 1..iterCount) {
                stacks[endIndex].push(stacks[startIndex].pop())
            }
        }
        for (stack in stacks)
            println(stack)
        var res = ""
        for (i in 0..8)
            res += stacks[i].pop()
        println(res)


    }

    fun part2() {
        var lines = Utils.getLines("5.txt")
        val stacks = MutableList(9) {
            Stack<Char>()
        }
        for (i in 7 downTo 0) {
            var stackId = 0
            for (x in 1..100 step 4) {
                if (stackId > 8)
                    break
                stacks[stackId].push(lines[i][x])
                stackId ++
            }
        }

        for (stack in stacks)
            stack.removeIf { it == ' ' }

        lines = lines.subList(10, lines.size)
        for (line in lines) {
            val iterCount = line.split("from")[0].replace("move", "").replace(" ", "").toInt()
            val startIndex = line.split("from")[1].replace(" ", "")[0].digitToInt()-1
            val endIndex = line.split("to")[1].replace(" ", "").last().digitToInt()-1
            println("$iterCount $startIndex $endIndex")
            val temp = Stack<Char>()
            for (interaction in 1..iterCount) {
                temp.push(stacks[startIndex].pop())
            }
            while (!temp.empty()) {
                stacks[endIndex].push(temp.pop())
            }
        }
        for (stack in stacks)
            println(stack)
        var res = ""
        for (i in 0..8)
            res += stacks[i].pop()
        println(res)


    }
}

