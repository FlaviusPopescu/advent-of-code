import Day21.solve1
import Day21.solve2Recursive
import Day21.solve2RecursiveCompact

const val WIN_SCORE_PART_1 = 1_000
const val WIN_SCORE_PART_2 = 21

/**
 * https://adventofcode.com/2021/day/21
 */
object Day21 {
    fun solve1(deterministicDie: Die = DeterministicDie()) {
        val players = input.let { (pos1, pos2) ->
            listOf(Player("1", pos1), Player("2", pos2))
        }

        while (true) {
            for (p in players) {
                p.rollThree(deterministicDie)
                if (p.score >= WIN_SCORE_PART_1) {
                    println(
                        "Player ${p.name} wins! Losing player rolls=${
                            players.first { it != p }.score * deterministicDie.rolls
                        }"
                    )
                    return
                }
            }
        }
    }

    fun solve2Recursive(winScore: Int = WIN_SCORE_PART_2) {
        val (p1, p2) = input
        data class State(
            val positions: List<Int>,
            val scores: List<Int>
        )

        val cache = HashMap<State, LongArray>()
        var (hits, misses) = 0 to 0
        fun totalWins(state: State): LongArray =
            state.run {
                cache[this]?.let { return it.also { hits++ } }
                val result = LongArray(positions.size)
                for (roll1 in 1..3) for (roll2 in 1..3) for (roll3 in 1..3) {
                    val roll = roll1 + roll2 + roll3
                    val pos = (positions.first() - 1 + roll) % 10 + 1
                    val score = scores.first() + pos
                    if (score >= winScore) {
                        result[0]++
                    } else {
                        totalWins(
                            State(
                                positions = positions.drop(1) + listOf(pos),
                                scores = scores.drop(1) + listOf(score)
                            )
                        ).let { shiftedResults ->
                            result[0] += shiftedResults.last()
                            for (i in 1 until result.size) {
                                result[i] += shiftedResults[i - 1]
                            }
                        }
                    }
                }
                result.also {
                    cache[state] = it
                    misses++
                }
            }
        totalWins(State(listOf(p1, p2), listOf(0, 0))).also {
            println("Scores: ${it.toList()}")
            println("Winner: ${it.maxOrNull()}")
        }
        println("Hits/Misses: $hits/$misses")
    }

    fun solve2RecursiveCompact(winScore: Int = WIN_SCORE_PART_2) {
        val (p1, p2) = input
        data class State(
            val positions: List<Int>,
            val scores: List<Int>,
            val multiplier: Long
        )

        fun totalWins(state: State): LongArray =
            state.run {
                val result = LongArray(positions.size)
                for (roll in 3..9) {
                    val ways = sumCounts[roll]!!.toLong()
                    val pos = (positions.first() - 1 + roll) % 10 + 1
                    val score = scores.first() + pos
                    if (score >= winScore) {
                        result[0] = result[0] + multiplier * ways
                    } else {
                        totalWins(
                            State(
                                positions = positions.drop(1) + listOf(pos),
                                scores = scores.drop(1) + listOf(score),
                                multiplier = multiplier * ways
                            )
                        ).let { shiftedResults ->
                            result[0] += shiftedResults.last()
                            for (i in 1 until result.size) {
                                result[i] += shiftedResults[i - 1]
                            }
                        }
                    }
                }
                result
            }
        totalWins(State(listOf(p1, p2), listOf(0, 0), 1L)).also {
            println("Scores: ${it.toList()}")
            println("Winner: ${it.maxOrNull()}")
        }
    }

    private val sumCounts = mutableMapOf<Int, Int>().apply {
        fun Int.numberOfWays(i: Int, rolls: Array<Int>): Int {
            if (i == 3) return (if (rolls.sum() == this) 1 else 0)
            var total = 0
            for (r in 1..3) {
                rolls[i] = r
                total += numberOfWays(i + 1, rolls)
            }
            return total
        }
        for (roll in 3..9) {
            this[roll] = roll.numberOfWays(0, arrayOf(0, 0, 0))
        }
    }.also { println(it) }

    private val input = with("""Player \d starting position: (\d+)""".toRegex()) {
        Day21.inputFromFile { line ->
            matchEntire(line)!!.destructured.let { (startPos) -> startPos.toInt() }
        }
    }
}

interface Die {
    fun roll(): Int
    var rolls: Int
}

class DeterministicDie : Die {
    private var i = 1
    override var rolls = 0
    override fun roll() = i++.also {
        if (i > 100) i = 1
        rolls++
    }
}

data class Player(
    val name: String,
    var position: Int,
    var score: Int = 0
) {
    fun rollThree(die: Die) {
        val total = (1..3).fold(0) { acc, _ -> acc + die.roll() }
        add(total)
    }

    fun add(roll: Int) {
        position = (position - 1 + roll) % 10 + 1
        score += position
    }
}

fun main() {
    printRunningTime("Part 1") { solve1() }
    printRunningTime("Part 2 - Recursive (individual rolls)") { solve2Recursive() }
    printRunningTime("Part 2 - Recursive (cumulated rolls, no caching)") {
        solve2RecursiveCompact()
    }
}
