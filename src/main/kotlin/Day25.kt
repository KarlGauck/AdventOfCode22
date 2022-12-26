import kotlin.math.pow
import kotlin.math.abs

object Day25 {

    fun part1() {
        val Dec = Utils.getLines("25").fold(0L) { r, it -> r + SNAFUToDec(it) }
        println(Dec)
        val snafu = DecToSNAFU(Dec)
        println(snafu)
        println(SNAFUToDec(snafu))
        println(DecToSNAFU(36671616971741))
    }

    private fun SNAFUToDec(snafu: String): Long = snafu.foldIndexed(0L) { i, r, it ->
        val dig = snafu.length-i-1
        r + when (it) {
            '-' -> -1
            '=' -> -2
            else -> it.digitToInt()
        } * 5.toDouble().pow(dig).toLong()
    }

    private fun DecToSNAFU(dec: Long): String {
        var dig = 0
        while (true) {
            val base = 5.0.pow(dig).toLong()
            if (dec / base in -2..2)
                break
            dig++
        }

        var running = dec
        var snafu = ""
        for (d in dig downTo 0) {
            val base = 5.0.pow(d).toLong()
            val lowerRange = -2L*base+1  until 2L*base
            val factor = (-2..2).map { base * it }.filter { running-it in lowerRange }.minBy { abs(running-it) } / base
            snafu += when(factor) {
                -2L -> '='
                -1L -> '-'
                else -> factor.toString()
            }
            running -= factor*base
        }

        return snafu
    }
}