/**
 * Reads all input in the file corresponding to [this] object's class name and returns it
 * as a list of the given type parameter.
 *
 * The following assumptions are made about how the solution files and input files:
 * - the input fits in memory
 * - each solution is defined in a class/object
 * - each problem input is in a file with the same name as the solver object, suffixed with ".in"
 * - each problem input file is in the resource directory of that same project module
 * - each line in the input file contains the same number of elements, to be processed by
 * the [parseInput] function; the function may return null if the result should be excluded
 * from the final output.
 *
 * e.g. `class Day01` may have a function that invokes [inputFromFile] and expects the file
 * "Day01.in" to be in the resource directory.
 */
fun <InputType> Any.inputFromFile(parseInput: (String) -> InputType?): List<InputType> {
    return this::class.run { java.getResourceAsStream("$simpleName.in") }
        .reader()
        .readLines()
        .mapNotNull { parseInput(it) }
}
