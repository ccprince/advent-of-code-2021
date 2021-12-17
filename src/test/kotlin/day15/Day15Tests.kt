package day15

import org.junit.jupiter.api.Disabled
import kotlin.test.Test
import kotlin.test.assertEquals

class Day15Tests {

    private val sample = parseCavern(
        listOf(
            "1163751742",
            "1381373672",
            "2136511328",
            "3694931569",
            "7463417111",
            "1319128137",
            "1359912421",
            "3125421639",
            "1293138521",
            "2311944581",
        )
    )

    private val simpleCavern = SimpleCavern(sample)
    private val expandedCavern = ExpandedCavern(sample)

    @Test @Disabled("There are multiple paths with the same risk")
    fun `finds the lowest-risk path`() = assertEquals(
        listOf(
            Pair(0, 0), Pair(0, 1), Pair(0, 2), Pair(1, 2), Pair(2, 2), Pair(3, 2), Pair(4, 2), Pair(5, 2),
            Pair(6, 2), Pair(6, 3), Pair(7, 3), Pair(7, 4), Pair(7, 5), Pair(8, 5), Pair(8, 6), Pair(8, 7),
            Pair(8, 8), Pair(9, 8), Pair(9, 9)
        ),
        leastRiskyPath(SimpleCavern(sample))
    )

    @Test
    fun `finds the risk of the best path`() =
        assertEquals(40, simpleCavern.riskOfPath(leastRiskyPath(simpleCavern)))

    @Test
    fun `finds the risk of the best path in an expanded cavern`() =
        assertEquals(315, expandedCavern.riskOfPath(leastRiskyPath(expandedCavern)))

    @Test
    fun `calculates the risk of an expanded cavern`() {
        val expected = listOf(
            listOf(8, 9, 1, 2, 3),
            listOf(9, 1, 2, 3, 4),
            listOf(1, 2, 3, 4, 5),
            listOf(2, 3, 4, 5, 6),
            listOf(3, 4, 5, 6, 7),
        )
        val cavern = ExpandedCavern(sample)
        val actual = (0..4).map { y ->
            (0..4).map { x -> cavern.riskAt(2 + x * 10, 1 + y * 10) }
        }
        assertEquals(expected, actual)
    }
}