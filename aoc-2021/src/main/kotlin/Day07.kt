import Day07.solvePart1
import Day07.solvePart2
import kotlin.math.abs

/**
 * https://adventofcode.com/2021/day/7
 */
object Day07 {
    fun solvePart1() = solveWithFuelModel { fromX, toX -> abs(fromX - toX) }
    fun solvePart2() = solveWithFuelModel { fromX, toX ->
        val n = abs(fromX - toX)
        n * (n + 1) / 2
    }

    private inline fun solveWithFuelModel(fuelConsumption: (from: Int, to: Int) -> Int) =
        (maxPosition downTo minPosition).minOf { targetPosition ->
            horizontalPositions.fold(0) { total, crabPosition ->
                total + fuelConsumption.invoke(targetPosition, crabPosition)
            }
        }

    private val horizontalPositions = inputFromFile {
        it.split(",").map { positions -> positions.toInt() }
    }.first().also {
        println("Read ${it.size} positions.")
    }

    private val minPosition: Int = horizontalPositions.minOrNull()
        .also { println("Min position = $it") }
        ?: throw UnsupportedOperationException("No input")

    private val maxPosition: Int = horizontalPositions.maxOrNull()
        .also { println("Max position = $it") }
        ?: throw UnsupportedOperationException("No input")
}

fun main() {
    printRunningTime("solved part 1") {
        solvePart1().also {
            println("$it")
        }
    }
    printRunningTime("solved part 2") {
        solvePart2().also {
            println("$it")
        }
    }
}
