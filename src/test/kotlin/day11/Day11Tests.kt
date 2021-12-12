package day11

import day09.parseArray
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11Tests {

    private val sample = parseArray(listOf(
        "5483143223",
        "2745854711",
        "5264556173",
        "6141336146",
        "6357385478",
        "4167524645",
        "2176841721",
        "6882881134",
        "4846848554",
        "5283751526",
    ))

    @Test
    fun `can count flashes`() = assertEquals(1656, countFlashes(sample, 100))

    @Test
    fun `can find the first synchronized flash`() = assertEquals(195, firstSynchronizedFlash(sample))
}