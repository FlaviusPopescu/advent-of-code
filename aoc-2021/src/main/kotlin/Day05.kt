import Day05.solve
import kotlin.math.abs

/**
 * https://adventofcode.com/2021/day/5
 */
object Day05 {
    fun solve() {
        val size = 1000
        val grid = Array(size) { Array(size) { 0 } }
        inputFromFile { line ->
            line.split(" -> ")
                .let { (firstPoint, secondPoint) ->
                    fun String.parseCoordinates() = split(",")
                        .map { coord -> coord.toInt() }
                    firstPoint.parseCoordinates() + secondPoint.parseCoordinates()
                }
                .let { (x1, y1, x2, y2) ->
                    when {
                        x1 == x2 -> {
                            for (i in minOf(y1, y2)..maxOf(y1, y2)) {
                                grid[x1][i]++
                            }
                        }
                        y1 == y2 -> {
                            for (i in minOf(x1, x2)..maxOf(x1, x2)) {
                                grid[i][y1]++
                            }
                        }
                        abs(x2 - x1) == abs(y2 - y1) -> {
                            val (startX, startY, endX, endY) = if (x1 < x2) {
                                listOf(x1, y1, x2, y2)
                            } else {
                                listOf(x2, y2, x1, y1)
                            }
                            var y = startY
                            val yDirection = if (endY > startY) 1 else -1
                            for (x in startX..endX) {
                                grid[x][y]++
                                y += yDirection
                            }
                        }
                    }
                }
        }
        grid.fold(0) { acc, row ->
            return@fold acc + row.sumOf { count -> (if (count >= 2) 1 else 0).toInt() }
        }.also {
            println("Result: $it")
        }
    }
}

fun main() {
    solve()
}
