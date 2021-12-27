import Day21.solve1
import Day21.solve2

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

    fun solve2() {
        val (playerOneStart, playerTwoStart) = input.let { (pos1, pos2) ->
            Player("1", pos1, 0) to Player("2", pos2, 0)
        }
        fun getWins(
            turn: Int,
            players: List<Player>,
            multipliers: List<ULong>
        ): List<ULong> {
            val results = mutableListOf(0uL, 0uL)
            for (roll in 3..9) {
                val ways = sumCounts[roll]!!.toULong()

                val updated = players.map { it.copy() }
                updated[turn].add(roll)

                if (updated[turn].score >= WIN_SCORE_PART_2) {
                    results[turn] += ways * multipliers[turn]
                } else {
                    val ms = mutableListOf(0uL, 0uL)
                    ms[turn] = multipliers[turn] * ways
                    ms[1 - turn] = multipliers[1 - turn] * ways
                    getWins(1 - turn, updated, ms).let { (p1, p2) ->
                        results[0] += p1
                        results[1] += p2
                    }
                }
            }
            return results
        }
        getWins(0, listOf(playerOneStart, playerTwoStart), listOf(1uL, 1uL)).also { (p1, p2) ->
            println("Part 2 Scores: $p1, $p2")
            println("Part 2 Winning Score: ${maxOf(p1, p2)}")
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
    solve1()
    solve2()
}
