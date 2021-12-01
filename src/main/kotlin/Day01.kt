/**
 * https://adventofcode.com/2021/day/1
 */
object Day01 {
    fun solvePart1(): Int {
        var depthIncreases = 0
        this::class.run {
            java.getResourceAsStream("$simpleName.in").reader().forEachLine { newLine ->
                depthIncreases += if (checkDepthIncreased(newLine) == true) 1 else 0
            }
        }
        return depthIncreases
    }

    private var previousDepth = Int.MAX_VALUE
    private fun checkDepthIncreased(newReading: String) = newReading.trim().toIntOrNull()
        ?.run {
            (this > previousDepth)
                .also { previousDepth = this }
        }
}

fun main() {
    println("Number of depth increases: ${Day01.solvePart1()}")
}
