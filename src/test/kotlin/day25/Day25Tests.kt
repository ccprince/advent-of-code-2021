package day25

import kotlin.test.Test
import kotlin.test.assertEquals

class Day25Tests {

    @Test
    fun `can find herds in the input data`() {
        val input = listOf(
            "..........",
            ".>v....v..",
            ".......>..",
            "..........",
        )

        assertEquals(
            setOf(Point(1, 1), Point(7, 2)),
            findHerd('>', input)
        )

        assertEquals(
            setOf(Point(2, 1), Point(7, 1)),
            findHerd('v', input)
        )
    }

    @Test
    fun `members of a single herd block each other`() = assertEquals(
        Triple(
            setOf(Point(3, 0), Point(4, 0), Point(5, 0), Point(7, 0), Point(9, 0)),
            setOf(),
            2
        ),
        moveCucumbers(listOf("...>>>>>..."), 2)
    )

    @Test
    fun `east-facing herd moves first`() = assertEquals(
        Triple(
            setOf(Point(1, 1), Point(8, 2)),
            setOf(Point(2, 2), Point(7, 2)),
            1
        ),
        moveCucumbers(
            listOf(
                "..........",
                ".>v....v..",
                ".......>..",
                "..........",
            ),
            1
        )
    )

    @Test
    fun `cucumbers wrap around the sea floor`() = assertEquals(
        Triple(
            setOf(Point(0, 0), Point(2, 2), Point(1, 3), Point(3, 4)),
            setOf(Point(2, 1), Point(4, 2), Point(3, 3), Point(0, 6)),
            4
        ),
        moveCucumbers(
            listOf(
                "...>...",
                ".......",
                "......>",
                "v.....>",
                "......>",
                ".......",
                "..vvv..",
            ),
            4
        )
    )

    @Test
    fun `cucumbers move until they are all blocked`() {
        val input = listOf(
            "v...>>.vv>",
            ".vv>>.vv..",
            ">>.>v>...v",
            ">>v>>.>.v.",
            "v>v.vv.v..",
            ">.>>..v...",
            ".vv..>.>v.",
            "v.v..>>v.v",
            "....v..v.>",
        )
        val (_, _, end) = moveCucumbers(input)
        assertEquals(58, end)
    }
}