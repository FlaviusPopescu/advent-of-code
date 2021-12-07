import kotlin.system.measureTimeMillis

inline fun printRunningTime(stage: String, block: () -> Unit) {
    measureTimeMillis(block).also { millis ->
        println("[${millis / 1_000f}sec] $stage")
    }
}
