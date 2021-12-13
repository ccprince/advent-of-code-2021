package day13

import kotlin.test.Test
import kotlin.test.assertEquals

class Day13Tests {

    val sample = parseInput(
        listOf(
            "6,10",
            "0,14",
            "9,10",
            "0,3",
            "10,4",
            "4,11",
            "6,0",
            "6,12",
            "4,1",
            "0,13",
            "10,12",
            "3,4",
            "3,0",
            "8,4",
            "1,10",
            "2,14",
            "8,10",
            "9,0",
            "",
            "fold along y=7",
            "fold along x=5",
        )
    )

    @Test
    fun `can fold a transparent piece of paper`() = assertEquals(
        setOf(
            Pair(0, 0), Pair(1, 0), Pair(2, 0), Pair(3, 0), Pair(4, 0),
            Pair(0, 1), Pair(4, 1),
            Pair(0, 2), Pair(4, 2),
            Pair(0, 3), Pair(4, 3),
            Pair(0, 4), Pair(1, 4), Pair(2, 4), Pair(3, 4), Pair(4, 4)
        ),
        sample.first.doFolds(sample.second)
    )

    @Test
    fun `can fold a single dot up`() = assertEquals(
        setOf(Pair(0, 0)),
        setOf(Pair(0, 14)).foldPaperUp(7)
    )

    @Test
    fun `can fold a single dot left`() = assertEquals(
        setOf(Pair(1, 0)),
        setOf(Pair(9, 0)).foldPaperLeft(5)
    )
}
