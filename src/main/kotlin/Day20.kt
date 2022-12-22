import java.util.LinkedList
object Day20 {

    fun part1() {
        val nums = Utils.getLines("20").map { Val(it.toLong()) }
        val list = LinkedList(nums)
        for (num in nums) {
            var index = (list.indexOf(num) + num.value) % (list.size-1)
            if (index < 0) index += list.size-1
            println(" -> $index")
            list.remove(num)
            list.add(index.toInt(), num)
        }

        println((1..3).fold(0L) { r, it ->
            val zeroEl = list.first { it.value == 0L }
            var index = (list.indexOf(zeroEl) + 1000*it) % (list.size)
            if (index < 0) index += list.size-1
            println(list[index].value)
            r + list[index].value
        })
    }

    fun part2() {
        val nums = Utils.getLines("20").map { Val(it.toLong() * 811589153) }
        val list = LinkedList(nums)
        for (mix in 0 until 10)
            for (num in nums) {
                var index = (list.indexOf(num) + num.value) % (list.size-1)
                if (index < 0) index += list.size-1
                list.remove(num)
                list.add(index.toInt(), num)
            }

        println((1..3).fold(0L) { r, it ->
            val zeroEl = list.first { it.value == 0L }
            var index = (list.indexOf(zeroEl) + 1000*it) % (list.size)
            if (index < 0) index += list.size-1
            println(list[index].value)
            r + list[index].value
        })
    }

    class Val(var value: Long)

}