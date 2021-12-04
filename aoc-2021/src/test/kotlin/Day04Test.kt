import Day04.checkColumns
import Day04.checkLines
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class Day04Test {
    @Test
    fun checkLineColumn() {
        arrayOf(
            arrayOf(false, true, true, true, true),
            arrayOf(true, true, false, true, true),
            arrayOf(true, true, false, true, true),
            arrayOf(true, true, true, false, true),
            arrayOf(true, true, true, true, false)
        ).checkColumns().also {
            assertTrue(it)
        }

        arrayOf(
            arrayOf(false, false, true, true, true),
            arrayOf(true, true, true, true, true),
            arrayOf(true, true, false, true, true),
            arrayOf(true, true, true, false, true),
            arrayOf(true, true, true, true, false)
        ).checkLines().also {
            assertTrue(it)
        }

        arrayOf(
            arrayOf(false, false, true, true, true),
            arrayOf(true, true, true, false, true),
            arrayOf(true, true, false, true, true),
            arrayOf(true, true, true, false, true),
            arrayOf(true, true, true, true, false)
        ).checkLines().also {
            assertFalse(it)
        }
    }
}