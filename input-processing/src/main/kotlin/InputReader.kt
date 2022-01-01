/**
 * Usage: `class Day01` members invoke [inputFromFile] and expect the file
 * "Day01.in" in the resource directory. Each line is mapped according to [parseInput]
 */
inline fun <InputType> Any.inputFromFile(
    crossinline parseInput: (String) -> InputType?
): List<InputType> {
    return this::class.run { java.getResourceAsStream("$simpleName.in") }
        .reader()
        .readLines()
        .mapNotNull(parseInput)
}

inline fun <InputType> Any.inputFromFileIndexed(
    crossinline parseInput: (Int, String) -> InputType?
): List<InputType> {
    return this::class.run { java.getResourceAsStream("$simpleName.in") }
        .reader()
        .readLines()
        .mapIndexedNotNull(parseInput)
}
