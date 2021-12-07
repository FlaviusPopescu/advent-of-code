import Day06.solvePart1
import Day06.solvePart2

/**
 * https://adventofcode.com/2021/day/6
 */
object Day06 {
    fun solvePart1() {
        val endDay = 80
        printRunningTime("Generate day $endDay list iteratively.") {
            getLanternFishTimers().toMutableList()
                .spawnIteratively(currentDay = 0, endDay, verbose = true)
        }
        printRunningTime("Count day $endDay descendants recursively.") {
            getLanternFishTimers().countAll(endDay).also {
                println("--> Day $endDay result: $it")
            }
        }
    }

    fun solvePart2() {
        val endDay = 256
        printRunningTime("Count day $endDay descendants recursively.") {
            getLanternFishTimers().countAll(endDay).also {
                println("--> Day $endDay result: $it")
            }
        }
    }

    private fun MutableList<Int>.spawnIteratively(currentDay: Int, endDay: Int, verbose: Boolean = false): Int {
        if (verbose) println(
            "${if (currentDay == 0) "Initial state" else "After day $currentDay"}: " +
                    "there are ${this.size} total fish"
        )
        return when (currentDay) {
            endDay -> size
            else -> {
                val startSize = this.size
                for (i in 0 until startSize) {
                    when {
                        this[i] > 0 -> this[i]--
                        else -> {
                            this[i] = 6
                            add(8)
                        }
                    }
                }
                spawnIteratively(currentDay + 1, endDay, verbose)
            }
        }
    }

    private fun List<Int>.countAll(endDay: Int) = also { cache.clear() }
        .fold(0L) { total, each ->
            total + each.count(endDay, newFish = true)
        }

    private val cache = mutableMapOf<Pair<Int, Int>, Long>()

    /**
     * Recursively counts all descendants of [this] fish's "timer".
     * Count each [newFish] once, and add the descendants it spawns every 7 days along with the
     * descendants of those descendants, counting each [newFish] only once.
     */
    private fun Int.count(endDay: Int, newFish: Boolean): Long {
        cache[this to endDay]?.let { cached ->
            return cached
        }
        return when {
            endDay < 0 -> 0
            else -> (if (newFish) 1L else 0L) +
                    6.count(endDay - this - 1, false) +
                    8.count(endDay - this - 1, true)
        }.also { result ->
            cache[this to endDay] = result
        }
    }

    private fun getLanternFishTimers() = inputFromFile {
        it.split(",").map { fishTimer -> fishTimer.toInt() }
    }.first()
}

fun main() {
    solvePart1()
    solvePart2()
}
