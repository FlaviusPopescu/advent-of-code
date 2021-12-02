/**
 * https://adventofcode.com/2021/day/1
 */
object Day01 {
    fun solvePart1(): Int = inputFromFile(::parseLine).totalIncreases()

    fun solvePart2(): Int = inputFromFile(::parseLine)
        .windowed(size = 3)
        .map { window -> window.sum() }
        .totalIncreases()

    private fun List<Int>.totalIncreases(): Int = windowed(size = 2)
        .sumOf { (previousDepth, currentDepth) ->
            (if (currentDepth > previousDepth) 1 else 0).toInt()
        }

    private fun parseLine(line: String): Int = line.trim().toIntOrNull()
        ?: throw IllegalArgumentException("Unexpected input=[$line]")
}

fun main() {
    println("Part 1: Number of depth increases: ${Day01.solvePart1()}")
    println("Part 2: Number of windowed increases: ${Day01.solvePart2()}")
}
