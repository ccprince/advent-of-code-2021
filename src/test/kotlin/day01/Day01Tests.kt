package day01

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day01Tests {

    val sample = listOf(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)

    @Test
    fun `can count increases`() {
        assertEquals(7, countIncreases(sample))
    }

    @Test
    fun `can count increases windowed by 3`() =
        assertEquals(5, countIncreasesBy3(sample))
}
