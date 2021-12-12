package day04

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day04Tests {

    val numbers1 = listOf(
        listOf(22, 13, 17, 11, 0),
        listOf(8, 2, 23, 4, 24),
        listOf(21, 9, 14, 16, 7),
        listOf(6, 10, 3, 18, 5),
        listOf(1, 12, 20, 15, 19)
    )

    val numbers2 = listOf(
        listOf(3, 15, 0, 2, 22),
        listOf(9, 18, 13, 17, 5),
        listOf(19, 8, 7, 25, 23),
        listOf(20, 11, 10, 24, 4),
        listOf(14, 21, 16, 12, 6)
    )

    val numbers3 = listOf(
        listOf(14, 21, 17, 24, 4),
        listOf(10, 16, 15, 9, 19),
        listOf(18, 8, 23, 26, 20),
        listOf(22, 11, 13, 6, 5),
        listOf(2, 0, 12, 3, 7)
    )

    val sequence = listOf(7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1)

    @Test
    fun `can mark a number`() = assertEquals(
        BingoCard(numbers1, setOf(Pair(4, 0))),
        markCard(BingoCard(numbers1), 1)
    )

    @Test
    fun `can mark a missing number`() = assertEquals(
        BingoCard(numbers3),
        markCard(BingoCard(numbers3), 1)
    )

    @Test
    fun `can mark another number`() = assertEquals(
        BingoCard(numbers3, setOf(Pair(0, 0), Pair(0, 1))),
        markCard(BingoCard(numbers3, setOf(Pair(0, 1))), 14)
    )

    @Test
    fun `can mark an already-marked number`() = assertEquals(
        BingoCard(numbers3, setOf(Pair(4, 4))),
        markCard(BingoCard(numbers3, setOf(Pair(4, 4))), 7)
    )

    @Test
    fun `can mark a sequence`() = assertEquals(
        BingoCard(numbers2, setOf(Pair(1, 0), Pair(1, 4), Pair(2, 2), Pair(3, 1), Pair(3, 4))),
        markDraws(BingoCard(numbers2), sequence.take(5))
    )

    @Test
    fun `can play until the card wins`() = assertEquals(
        sequence.take(12),
        winningSequence(BingoCard(numbers3), sequence)
    )

    @Test
    fun `can determine a winning card`() = assertTrue(markDraws(BingoCard(numbers3), sequence.take(12)).isWinner())

    @Test
    fun `ignores filled diagonals when determining a winner`() =
        assertFalse(markDraws(BingoCard(numbers3), sequence.take(11)).isWinner())

    @Test
    fun `can score a marked card`() = assertEquals(188, markDraws(BingoCard(numbers3), sequence.take(12)).score())

    @Test
    fun `can score an empty card`() = assertEquals(300, BingoCard(numbers1).score())

    @Test
    fun `can evaluate the first winning card`() = assertEquals(
        4512,
        valueOfFirstWinningCard(listOf(BingoCard(numbers1), BingoCard(numbers2), BingoCard(numbers3)), sequence)
    )

    @Test
    fun `can evaluate the last winning card`() = assertEquals(
        1924,
        valueOfLastWinningCard(listOf(BingoCard(numbers1), BingoCard(numbers2), BingoCard(numbers3)), sequence)
    )
}
