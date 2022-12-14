import java.util.*
import kotlin.math.min

object Day13 {

    fun readInput(src: String) = Utils.getLines(src).split("")

    fun compare(l1const: String, l2const: String, d: Int = 0): Boolean? {
        var l1 = l1const
        var l2 = l2const
        val compareLists = "[" in l1 || "[" in l2
        if (compareLists) {
            if ("[" !in l1)
                l1 = "[$l1]"
            if ("]" !in l2)
                l2 = "[$l2]"
            var depth = 0
            var l1Elements = mutableListOf<String>()
            var lastComma = 0
            for (cI in l1.indices) {
                val c = l1[cI]
                when (c) {
                    '[' -> depth++
                    ']' -> depth--
                    ',' -> if (depth == 1) {
                        l1Elements.add(l1.substring(lastComma + 1 until cI))
                        lastComma = cI
                    }
                }
            }
            l1Elements.add(l1.substring(lastComma+1 until l1.length-1))
            var l2Elements = mutableListOf<String>()
            lastComma = 0
            for (cI in l2.indices) {
                val c = l2[cI]
                when (c) {
                    '[' -> depth++
                    ']' -> depth--
                    ',' -> if (depth==1) {
                        l2Elements.add(l2.substring(lastComma+1 until cI))
                        lastComma = cI
                    }
                }
            }
            l2Elements.add(l2.substring(lastComma+1 until l2.length-1))
            l1Elements = l1Elements.filter { it != "" }.toMutableList()
            l2Elements = l2Elements.filter { it != "" }.toMutableList()
            for (i in 0 until min(l1Elements.size, l2Elements.size)) {
                val res = compare(l1Elements[i], l2Elements[i], d+8)
                if (res != null)
                    return res
            }
            if (l1Elements.size < l2Elements.size)
                return true
            if (l2Elements.size < l1Elements.size)
                return false
            return null
        } else {
            if (l1.toInt() < l2.toInt()) return true
            else if (l2.toInt() < l1.toInt()) return false
            else return null
        }
    }

    fun part1() {
        val pairs = readInput("13.txt")

        var sum = 0
        for (pI in pairs.indices) {
            val pair = pairs[pI]
            if(compare(pair[0], pair[1])==true)
                sum += pI+1
        }
        println(sum)
    }

    fun part2() {
        var packets = Utils.getLines("13.txt").filter { it != "" }.plus(listOf("[[2]]", "[[6]]"))
        var swapped = true
        while (swapped) {
            swapped = false
            for (i in 1 until packets.size) {
                val res = compare(packets[i], packets[i-1])
                if (res == true) {
                    swapped = true
                    Collections.swap(packets, i-1, i)
                }
            }
        }
        println((packets.indexOf("[[2]]")+1) * (packets.indexOf("[[6]]")+1))
    }

}