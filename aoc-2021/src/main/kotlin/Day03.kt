/**
 * https://adventofcode.com/2021/day/3
 */
object Day03 {
    fun solvePart1() = inputFromFile(::parseLine).bitCounts().let { bitCounts ->
        val gamma = bitCounts.map { (zeros, ones) -> if (zeros > ones) '0' else '1' }
            .joinToString("").toInt(2)
        val epsilon = bitCounts.map { (zeros, ones) -> if (zeros < ones) '0' else '1' }
            .joinToString("").toInt(2)
        gamma * epsilon
    }

    fun solvePart2(): Int {
        fun List<String>.solve(position: Int, selector: (Pair<Int, Int>) -> Char): String {
            if (size == 1) {
                return first()
            }
            return bitCounts()[position].let { counts ->
                filter { it[position] == selector(counts) }.solve(position + 1, selector)
            }
        }

        val oxygen = inputFromFile(::parseLine).solve(position = 0) { (zeros, ones) ->
            if (ones >= zeros) '1' else '0'
        }.toInt(2)
        val co2 = inputFromFile(::parseLine).solve(position = 0) { (zeros, ones) ->
            if (zeros <= ones) '0' else '1'
        }.toInt(2)
        return oxygen * co2
    }

    private fun List<String>.bitCounts() = fold(mutableListOf<Pair<Int, Int>>()) { bitCounts, diagnostic ->
        if (bitCounts.isEmpty()) {
            repeat(diagnostic.length) { bitCounts.add(0 to 0) }
        }
        bitCounts.apply {
            diagnostic.forEachIndexed { i, character ->
                when (character) {
                    '0' -> bitCounts[i] = bitCounts[i].first + 1 to bitCounts[i].second
                    '1' -> bitCounts[i] = bitCounts[i].first to bitCounts[i].second + 1
                }
            }
        }
    }

    private fun parseLine(line: String) = line.trim()
}

fun main() {
    println(Day03.solvePart1())
    println(Day03.solvePart2())
}
