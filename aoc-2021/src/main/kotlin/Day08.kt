import Day08.SevenSegmentDisplay.*
import Day08.SevenSegmentDisplay.Companion.toUniqueValue
import Day08.SevenSegmentDisplay.Companion.toValue
import Day08.solvePart1
import Day08.solvePart2
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/**
 * https://adventofcode.com/2021/day/8
 */
object Day08 {
    fun solvePart1() = input.fold(0) { totalUniques, (_, outputSignals) ->
        totalUniques + outputSignals.sumOf { signal ->
            when (signal.length.toUniqueValue()) {
                UNKNOWN -> 0
                else -> 1
            }.toInt()
        }
    }.also { checkInputForPart2() }

    private fun checkInputForPart2() {
        input.all { (inputSignals, _) ->
            inputSignals.run {
                any { it.length.toUniqueValue() == ONE } &&
                        any { it.length.toUniqueValue() == FOUR } &&
                        any { it.length.toUniqueValue() == SEVEN } &&
                        any { it.length.toUniqueValue() == EIGHT }
            }
        }.let { friendlyInput ->
            when (friendlyInput) {
                true -> println("All input includes each unique pattern at least once.")
                false -> println("Failure: Some input does not have all unique patterns present.")
            }
        }
    }

    suspend fun solvePart2() {
        coroutineScope {
            input.mapIndexed { i, (inSignals, outSignals) ->
                async(Dispatchers.Default) {
                    var result = 0
                    allMappings.collect { candidate ->
                        candidate.takeIf { inSignals.matchesWith(it) }?.let { match ->
                            result = outSignals.decodeAll(match).toNumber()
                            println("Result for line $i is $result")
                        }
                    }
                    result
                }
            }.let { results ->
                awaitAll(*results.toTypedArray()).sum().also {
                    println("Sum: $it")
                }
            }
        }
    }

    private val allMappings: Flow<Map<Char, Int>>
        get() = flow {
            val chars = ('a'..'g').toSet()
            val binding = CharArray(7) { '0' }

            suspend fun generateBindings(k: Int, chars: Iterable<Char>) {
                if (k == binding.size) {
                    emit(binding.mapIndexed { i, c -> c to i }.toMap())
                }
                chars.forEach { c ->
                    binding[k] = c
                    generateBindings(k + 1, chars.filterNot { it == c })
                }
            }
            generateBindings(0, chars)
        }

    private fun List<String>.matchesWith(binding: Map<Char, Int>) =
        all { signal ->
            signal.map { binding[it] ?: return@all false }.toSet().toValue() != UNKNOWN
        }

    private fun List<String>.decodeAll(mapping: Map<Char, Int>) =
        map { signal ->
            signal.map { mapping[it]!! }.toSet().toValue()
        }

    /**
     * Display segments numbered top to bottom, left to right:
     *  000
     * 1   2
     * 1   2
     *  333
     * 4   5
     * 4   5
     *  666
     * [segmentsRequired] lists above segments.
     */
    private enum class SevenSegmentDisplay(vararg val segmentsRequired: Int) {
        UNKNOWN,
        ZERO(0, 2, 5, 6, 4, 1),
        ONE(2, 5),
        TWO(0, 2, 3, 4, 6),
        THREE(0, 2, 3, 5, 6),
        FOUR(1, 3, 2, 5),
        FIVE(0, 1, 3, 5, 6),
        SIX(0, 1, 3, 4, 6, 5),
        SEVEN(0, 2, 5),
        EIGHT(0, 1, 2, 3, 4, 5, 6),
        NINE(0, 1, 2, 3, 5, 6);

        val toDigit: Int get() = ordinal - 1

        companion object {
            private val valuesBySegmentCount = values()
                .associateBy { it.segmentsRequired.size }
            private val valuesBySegments = values()
                .associateBy { it.segmentsRequired.toSet() }

            fun Int.toUniqueValue() = valuesBySegmentCount[this].run {
                when (this) {
                    ONE,
                    FOUR,
                    SEVEN,
                    EIGHT -> {
                        this
                    }
                    else -> {
                        // no result (since the others may have similar segment numbers)
                        UNKNOWN
                    }
                }
            }

            fun Set<Int>.toValue() = valuesBySegments[this] ?: UNKNOWN
        }
    }

    private fun List<SevenSegmentDisplay>.toNumber() =
        map { it.toDigit }.fold(0) { result, digit ->
            result * 10 + digit
        }

    private val input = inputFromFile { line ->
        line.split(" | ").let { (inputList, outputList) ->
            inputList.trim().split(" ") to
                    outputList.trim().split(" ")
        }
    }
}

fun main() {
    println("Unique numbers: ${solvePart1()}")
    printRunningTime("Part 2") {
        runBlocking { solvePart2() }
    }
}
