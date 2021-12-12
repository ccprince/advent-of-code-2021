package day05

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day05Tests {

    private val sampleData = listOf(
        Line(0, 9, 5, 9),
        Line(8, 0, 0, 8), // Non-orthogonal
        Line(9, 4, 3, 4),
        Line(2, 2, 2, 1),
        Line(7, 0, 7, 4),
        Line(6, 4, 2, 0), // Non-orthogonal
        Line(0, 9, 2, 9),
        Line(3, 4, 1, 4),
        Line(0, 0, 8, 8), // Non-orthogonal
        Line(5, 5, 8, 2), // Non-orthogonal
    )

    @Test
    fun `parse Line from string`() = assertEquals(Line(12, 23, 34, 45), Line.fromString("12,23 -> 34,45"))

    @Test
    fun `can calculate an overlap of orthogonal lines`() = assertEquals(
        5,
        calculateOverlap(sampleData.filter { it.isOrthogonal })
    )

    @Test
    fun `can convert lines to points`() = assertEquals(
        listOf(
            listOf(Pair(0, 9), Pair(1, 9), Pair(2, 9), Pair(3, 9), Pair(4, 9), Pair(5, 9)),
            listOf(Pair(8, 0), Pair(7, 1), Pair(6, 2), Pair(5, 3), Pair(4, 4), Pair(3, 5), Pair(2, 6), Pair(1, 7), Pair(0, 8)),
            listOf(Pair(9, 4), Pair(8, 4), Pair(7, 4), Pair(6, 4), Pair(5, 4), Pair(4, 4), Pair(3, 4)),
            listOf(Pair(2, 2), Pair(2, 1)),
            listOf(Pair(7, 0), Pair(7, 1), Pair(7, 2), Pair(7, 3), Pair(7, 4)),
            listOf(Pair(6, 4), Pair(5, 3), Pair(4, 2), Pair(3, 1), Pair(2, 0)),
            listOf(Pair(0, 9), Pair(1, 9), Pair(2, 9)),
            listOf(Pair(3, 4), Pair(2, 4), Pair(1, 4)),
            listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2), Pair(3, 3), Pair(4, 4), Pair(5, 5), Pair(6, 6), Pair(7, 7), Pair(8, 8)),
            listOf(Pair(5, 5), Pair(6, 4), Pair(7, 3), Pair(8, 2))
        ),
        sampleData.map { it.toPoints() }
    )
}