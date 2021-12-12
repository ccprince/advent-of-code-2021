package day09

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test
import kotlin.test.assertEquals


class Day09Tests {

    private val sampleInput = listOf(
        "2199943210",
        "3987894921",
        "9856789892",
        "8767896789",
        "9899965678",
    )

    @Test
    fun `spot-check input parsing`() = assertEquals(listOf(3, 9, 8, 7, 8, 9, 4, 9, 2, 1), parseArray(sampleInput)[1])

    @Test
    fun `finds low spots`() = assertEquals(
        setOf(Pair(0, 1), Pair(0, 9), Pair(2, 2), Pair(4, 6)),
        lowSpots(parseArray(sampleInput))
    )

    @Test
    fun `calculates total risk`() = assertEquals(15, totalRisk(parseArray(sampleInput)))

    @TestFactory
    fun `finds basins`() = listOf(
        Pair(0, 1) to setOf(Pair(0, 1), Pair(0, 0), Pair(1, 0)),
        Pair(0, 9) to setOf(
            Pair(0, 5), Pair(0, 6), Pair(0, 7), Pair(0, 8), Pair(0, 9),
            Pair(1, 6), Pair(1, 8), Pair(1, 9), Pair(2, 9)
        ),
        Pair(2, 2) to setOf(
            Pair(1, 2), Pair(1, 3), Pair(1, 4), Pair(2, 1), Pair(2, 2), Pair(2, 3),
            Pair(2, 4), Pair(2, 5), Pair(3, 0), Pair(3, 1), Pair(3, 2), Pair(3, 3), Pair(3, 4), Pair(4, 1)
        ),
        Pair(4, 6) to setOf(
            Pair(2, 7), Pair(3, 6), Pair(3, 7), Pair(3, 8), Pair(4, 5), Pair(4, 6), Pair(4, 7),
            Pair(4, 8), Pair(4, 9)
        )
    ).map { (point, basin) ->
        DynamicTest.dynamicTest("basin at (${point.first}, ${point.second}) has ${basin.size} cells") {
            assertEquals(basin, basinAt(point, parseArray(sampleInput)))
        }
    }

    @TestFactory
    fun `finds basins recursively`() = listOf(
        Pair(0, 1) to setOf(Pair(0, 1), Pair(0, 0), Pair(1, 0)),
        Pair(0, 9) to setOf(
            Pair(0, 5), Pair(0, 6), Pair(0, 7), Pair(0, 8), Pair(0, 9),
            Pair(1, 6), Pair(1, 8), Pair(1, 9), Pair(2, 9)
        ),
        Pair(2, 2) to setOf(
            Pair(1, 2), Pair(1, 3), Pair(1, 4), Pair(2, 1), Pair(2, 2), Pair(2, 3),
            Pair(2, 4), Pair(2, 5), Pair(3, 0), Pair(3, 1), Pair(3, 2), Pair(3, 3), Pair(3, 4), Pair(4, 1)
        ),
        Pair(4, 6) to setOf(
            Pair(2, 7), Pair(3, 6), Pair(3, 7), Pair(3, 8), Pair(4, 5), Pair(4, 6), Pair(4, 7),
            Pair(4, 8), Pair(4, 9)
        )
    ).map { (point, basin) ->
        DynamicTest.dynamicTest("basin at (${point.first}, ${point.second}) has ${basin.size} cells") {
            assertEquals(basin, basinAt2(point, parseArray(sampleInput)))
        }
    }

}

