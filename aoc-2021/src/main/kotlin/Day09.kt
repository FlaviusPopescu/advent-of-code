import Day09.solvePart1
import Day09.solvePart2

/**
 * https://adventofcode.com/2021/day/9
 */
object Day09 {
    fun solvePart1() {
        var sum = 0
        for (i in grid.indices) {
            for (j in grid[i].indices) {
                if ((i to j).isLowPoint(grid)) sum += grid[i][j] + 1
            }
        }
        println(sum)
    }

    fun solvePart2() {
        val basinSizes = mutableListOf<Int>()
        for (i in grid.indices) {
            for (j in grid.first().indices) {
                if ((i to j).isLowPoint(grid)) {
                    val visited = grid.map { BooleanArray(grid.first().size) { false } }
                    fillBasin(i, j, grid, visited).run {
                        if (this > 0) {
                            basinSizes.add(this)
                            println("\nFound basin:${
                                visited.fold(StringBuilder()) { buffer, line ->
                                    buffer.append("\n")
                                    line.forEach { v ->
                                        buffer.append(if (v) "1" else "0")
                                    }
                                    buffer
                                }
                            }")
                        }
                    }
                }
            }
        }
        basinSizes.sorted().reversed().take(3).fold(1) { result, i -> result * i }.let {
            println("Product of top 3 of ${basinSizes.size} is: $it")
        }
    }

    private fun fillBasin(x: Int, y: Int, grid: List<List<Int>>, visited: List<BooleanArray>): Int {
        if (!(x to y).isLowPoint(grid, visited) || grid[x][y] == 9) return 0
        visited[x][y] = true
        var sum = 1
        for ((newX, newY) in (x to y).allNeighborsIn(grid)) {
            if (!visited[newX][newY]) {
                sum += fillBasin(newX, newY, grid, visited)
            }
        }
        return sum
    }

    private fun Pair<Int, Int>.isLowPoint(
        grid: List<List<Int>>,
        visited: List<BooleanArray> = emptyList()
    ) =
        let { (x, y) ->
            allNeighborsIn(grid)
                .filter { (newX, newY) ->
                    visited.takeIf { it.isNotEmpty() }
                        ?.let { visited ->
                            !visited[newX][newY]
                        } ?: true
                }
                .all { (neighborX, neighborY) ->
                    grid[x][y] <= grid[neighborX][neighborY]
                }
        }

    private val grid = inputFromFile {
        it.trim().map { ch -> ch.digitToInt() }
    }

    private fun Pair<Int, Int>.allNeighborsIn(grid: List<List<Int>>) =
        directions.mapNotNull { direction ->
            neighborOf(direction).takeIf { it.withinGrid(grid) }
        }

    private fun Pair<Int, Int>.neighborOf(direction: Pair<Int, Int>) =
        let { (currentX, currentY) ->
            val (deltaX, deltaY) = direction
            (currentX + deltaX to currentY + deltaY)
        }

    private fun Pair<Int, Int>.withinGrid(grid: List<List<Int>>) =
        let { (x, y) ->
            x >= 0 && x < grid.size && y >= 0 && y < grid.first().size
        }

    private val directions = listOf(
        -1 to 0,
        1 to 0,
        0 to -1,
        0 to 1
    )
}

fun main() {
    solvePart1()
    solvePart2()
}
