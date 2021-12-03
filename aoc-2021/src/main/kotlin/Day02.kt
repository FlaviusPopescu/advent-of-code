import Day02.Pose
import Day02.solvePart1
import Day02.solvePart2

/**
 * https://adventofcode.com/2021/day/2
 */
object Day02 {
    fun solvePart1() = inputFromFile(::parseLine).fold(Pose(0, 0)) { currentPose, (direction, delta) ->
        currentPose.run {
            when (direction) {
                Direction.FORWARD -> currentPose.copy(travel = travel + delta)
                Direction.UP -> currentPose.copy(depth = depth - delta)
                Direction.DOWN -> currentPose.copy(depth = depth + delta)
            }
        }
    }

    fun solvePart2() = inputFromFile(::parseLine)
        .fold(WackyPose(0, Pose(0, 0))) { currentWackyPose, (direction, delta) ->
            currentWackyPose.run {
                when (direction) {
                    Direction.FORWARD -> currentWackyPose.copy(
                        pose = pose.copy(
                            travel = pose.travel + delta,
                            depth = pose.depth + delta * aim
                        ),
                    )
                    Direction.UP -> currentWackyPose.copy(aim = aim - delta)
                    Direction.DOWN -> currentWackyPose.copy(aim = aim + delta)
                }
            }
        }

    data class Pose(
        val depth: Int,
        val travel: Int
    )

    data class WackyPose(
        val aim: Int,
        val pose: Pose
    )

    private fun parseLine(line: String) = line.trim().split(" ").let { (directionKey, value) ->
        val direction = Direction.parseDirection[directionKey] ?: return@let null
        val distance = value.toIntOrNull() ?: return@let null
        direction to distance
    }

    enum class Direction(val inputKey: String) {
        FORWARD("forward"),
        UP("up"),
        DOWN("down");

        companion object {
            val parseDirection = values().associateBy { it.inputKey }
        }
    }
}

fun main() {
    fun Pose.submarinePosePretty() = "Submarine pose: $this; result is ${depth * travel}"
    println("Part 1: ${solvePart1().submarinePosePretty()}")
    println("Part 2: ${solvePart2().pose.submarinePosePretty()}")
}
