package day03

import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class Day03Tests {

    private val sample = listOf(
        "00100",
        "11110",
        "10110",
        "10111",
        "10101",
        "01111",
        "00111",
        "11100",
        "10000",
        "11001",
        "00010",
        "01010",
    )

    @Test
    fun `can calculate the gamma rate`() = assertEquals(22, gammaRate(sample))

    @Test
    fun `can calculate the epsilon rate`() = assertEquals(9, epsilonRate(sample))

    @Test
    fun `can count the '1' bits`() = assertEquals(listOf(7, 5, 8, 7, 5), countOnes(sample))

    @Test
    fun `can count the most common bits`() = assertEquals(listOf('1', '0', '1', '1', '0'), mostCommonBits(sample))

    @Test
    fun `can count the least common bits`() = assertEquals(listOf('0', '1', '0', '0', '1'), leastCommonBits(sample))

    @Test
    fun `can calculate the oxygen generator rating`() = assertEquals(23, oxygenGeneratorRating(sample))

    @Test
    fun `can calculate the CO2 scrubber rating`() = assertEquals(10, co2ScrubberRating(sample))

    @Test
    fun `can filter a list of diagnostics`() = assertEquals("11110", repeatedFilter(sample, { _: Int, _: Int -> '1' }))
}
