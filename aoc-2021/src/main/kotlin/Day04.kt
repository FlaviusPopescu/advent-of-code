import Day04.solve
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion

/**
 * https://adventofcode.com/2021/day/4
 */
object Day04 {
    private val numbers = mutableListOf<Int>()
    private val boards = mutableListOf<Board>()

    suspend fun solve() {
        val winnings = mutableSetOf<Int>()
        val marks = boards.map {
            Array(5) {
                Array(5) { false }
            }
        }
        val numbersFlow = MutableSharedFlow<Int>()
        val players = boards.mapIndexed { boardIndex, board ->
            board to suspend {
                numbersFlow.collect { newNum: Int ->
                    if (boardIndex !in winnings) {
                        for (i in 0 until 5) {
                            for (j in 0 until 5) {
                                if (board[i][j] == newNum) {
                                    marks[boardIndex][i][j] = true
                                }
                            }
                        }
                        marks[boardIndex].run {
                            if (checkLines() || checkColumns()) {
                                println("found winner $boardIndex, board = ${boards[boardIndex]}")
                                var s = 0
                                for (i in 0 until 5) {
                                    for (j in 0 until 5) {
                                        if (!marks[boardIndex][i][j]) {
                                            s += boards[boardIndex][i][j]
                                        }
                                    }
                                }
                                println("result: ${s * newNum}")
                                winnings.add(boardIndex)
                            }
                        }
                    }
                }
            }
        }
        coroutineScope {
            val jobs = mutableListOf<Job>()
            players.forEach { (_, collector) ->
                jobs += launch(Dispatchers.IO) {
                    collector.invoke()
                }
            }
            numbers.forEach {
                numbersFlow.emit(it)
            }
            jobs.forEach { it.cancel() }
        }
    }

    internal fun Array<Array<Boolean>>.checkLines() = any { it.all { marked -> marked } }
    internal fun Array<Array<Boolean>>.checkColumns(): Boolean {
        for (j in 0 until 5) {
            var column = true
            for (i in 0 until 5) {
                column = column && this[i][j]
                if (!column) break
            }
            if (column) return true
        }
        return false
    }

    init {
        var currentBoard = mutableListOf<List<Int>>()
        inputFromFileIndexed { i, line ->
            when {
                i == 0 -> {
                    line.split(",").map { it.toInt() }.let {
                        numbers.addAll(it)
                    }
                }
                (i - 1) % 6 == 0 -> {
                    boards.add(currentBoard)
                    currentBoard = mutableListOf()
                }
                else -> {
                    currentBoard.add(
                        line.trim().split(" ")
                            .mapNotNull { it.takeIf { it.isNotEmpty() } }
                            .map { it.toInt() }
                    )
                }
            }
        }
        currentBoard.takeIf { it.isNotEmpty() }?.let {
            boards.add(it)
        }
        boards.removeIf { it.isEmpty() }
    }
}

typealias Board = MutableList<List<Int>>

fun main() {
    runBlocking { solve() }
}
