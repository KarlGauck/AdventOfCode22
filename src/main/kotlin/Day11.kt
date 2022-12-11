object Day11 {

    fun part1() {
        val monkeyStrings = Utils.getLines("11.txt").split("")
        val monkeys = Array(monkeyStrings.size) {Monkey()}
        for (mSI in monkeyStrings.indices) {
            val mS = monkeyStrings[mSI]
            val items = mS[1].replace("  Starting items: ", "").replace(" ", "").split(",").map { Item(it.toInt()) }.toMutableList()
            val operationString = mS[2].split("=")[1].split(" ").filter { it != "" }
            val testVar = mS[3].split(" ").last().toInt()
            val monkey1 = mS[4].split(" ").last().toInt()
            val monkey2 = mS[5].split(" ").last().toInt()
            val operation: (Item) -> Int = {
                val f = if (operationString[0]=="old") it.worry else operationString[0].toInt()
                val s = if (operationString[2]=="old") it.worry else operationString[2].toInt()
                (if (operationString[1]=="+") f + s else if (operationString[1]=="*") f * s else it.worry)/3
            }
            monkeys[mSI].testVar = testVar
            monkeys[mSI].operation = operation
            monkeys[mSI].monkey1 = monkey1
            monkeys[mSI].monkey2 = monkey2
            monkeys[mSI].items = items
        }

        for (i in 0 until 20) {
            for (mI in monkeys.indices) {
                val monkey = monkeys[mI]
                for (item in monkey.items) {
                    monkey.playCount ++
                    item.worry = monkey.operation(item)
                    if (item.worry % monkey.testVar == 0)
                        monkeys[monkey.monkey1].items.add(item)
                    else
                        monkeys[monkey.monkey2].items.add(item)
                }
                monkey.items = mutableListOf()
            }
        }

        val played = monkeys.sortedByDescending { it.playCount }.map { it.playCount }
        println("${played[0]} * ${played[1]} = ${played[0]*played[1]}")
    }

    fun part2() {
        val monkeyStrings = Utils.getLines("11.txt").split("")
        val monkeys = Array(monkeyStrings.size) {UMonkey()}
        val smallestFactor = monkeyStrings.fold(1) {r, it ->
            it[3].split(" ").last().toInt() * r
        }.toULong()
        for (mSI in monkeyStrings.indices) {
            val mS = monkeyStrings[mSI]
            val items = mS[1].replace("  Starting items: ", "").replace(" ", "").split(",").map { UItem(it.toULong()) }.toMutableList()
            val operationString = mS[2].split("=")[1].split(" ").filter { it != "" }
            val testVar = mS[3].split(" ").last().toInt()
            val monkey1 = mS[4].split(" ").last().toInt()
            val monkey2 = mS[5].split(" ").last().toInt()
            val operation: (UItem) -> ULong = {
                val f = if (operationString[0]=="old") it.worry else operationString[0].toULong()
                val s = if (operationString[2]=="old") it.worry else operationString[2].toULong()
                val r = (if (operationString[1]=="+") f + s else if (operationString[1]=="*") f * s else it.worry)
                r%smallestFactor
            }
            monkeys[mSI].testVar = testVar
            monkeys[mSI].operation = operation
            monkeys[mSI].monkey1 = monkey1
            monkeys[mSI].monkey2 = monkey2
            monkeys[mSI].items = items
        }

        for (i in 0 until 10000) {
            for (mI in monkeys.indices) {
                val monkey = monkeys[mI]
                for (item in monkey.items) {
                    monkey.playCount ++
                    val oldworry = item.worry
                    item.worry = monkey.operation(item)
                    if (item.worry % monkey.testVar.toULong() == 0.toULong())
                        monkeys[monkey.monkey1].items.add(item)
                    else
                        monkeys[monkey.monkey2].items.add(item)
                }
                monkey.items = mutableListOf()
            }
        }

        val played = monkeys.sortedByDescending { it.playCount }.map { it.playCount }
        println("${played[0]} * ${played[1]} = ${played[0].toULong()*played[1].toULong()}")
    }

}

class Monkey {
    var playCount: Int = 0
    var testVar: Int = 0
    var monkey1: Int = 0
    var monkey2: Int = 0
    var items = mutableListOf<Item>()
    var operation: (Item) -> Int = {0}
}

class Item(var worry: Int)

class UMonkey {
    var playCount: Int = 0
    var testVar: Int = 0
    var monkey1: Int = 0
    var monkey2: Int = 0
    var items = mutableListOf<UItem>()
    var operation: (UItem) -> ULong = {0.toULong()}
}

class UItem(var worry: ULong)