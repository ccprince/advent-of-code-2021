package day23

import day23.Amphipod.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day23Tests {

    private val sampleStart = listOf(
        "#############",
        "#...........#",
        "###B#C#B#D###",
        "  #A#D#C#A#",
        "  #########",
    )

    private val inProgress = listOf(
        "#############",
        "#...B.......#",
        "###B#.#C#D###",
        "  #A#D#C#A#",
        "  #########",
    )

    private val organized = listOf(
        "#############",
        "#...........#",
        "###A#B#C#D###",
        "  #A#B#C#D#",
        "  #########",
    )

    private val emptyDestination = listOf(
        "#############",
        "#DD........C#",
        "###A#B#.#.###",
        "  #A#B#C#.#",
        "  #########",
    )

    private val largeSample = listOf(
        "#############",
        "#...........#",
        "###B#C#B#D###",
        "  #D#C#B#A#",
        "  #D#B#A#C#",
        "  #A#D#C#A#",
        "  #########",
    )


    @Test
    fun `can parse a starting position`() = assertEquals(
        mapOf(
            Location(2, 1) to BRONZE,
            Location(2, 2) to AMBER,
            Location(4, 1) to COPPER,
            Location(4, 2) to DESERT,
            Location(6, 1) to BRONZE,
            Location(6, 2) to COPPER,
            Location(8, 1) to DESERT,
            Location(8, 2) to AMBER
        ),
        parseState(sampleStart)
    )

    @Test
    fun `can parse a position with something in the hall`() = assertEquals(
        mapOf(
            Location(2, 1) to BRONZE,
            Location(2, 2) to AMBER,
            Location(3, 0) to BRONZE,
            Location(4, 2) to DESERT,
            Location(6, 1) to COPPER,
            Location(6, 2) to COPPER,
            Location(8, 1) to DESERT,
            Location(8, 2) to AMBER
        ),
        parseState(inProgress)
    )

    @Test
    fun `can parse a large starting position`() = assertEquals(
        mapOf(
            Location(2, 1) to BRONZE,
            Location(2, 2) to DESERT,
            Location(2, 3) to DESERT,
            Location(2, 4) to AMBER,
            Location(4, 1) to COPPER,
            Location(4, 2) to COPPER,
            Location(4, 3) to BRONZE,
            Location(4, 4) to DESERT,
            Location(6, 1) to BRONZE,
            Location(6, 2) to BRONZE,
            Location(6, 3) to AMBER,
            Location(6, 4) to COPPER,
            Location(8, 1) to DESERT,
            Location(8, 2) to AMBER,
            Location(8, 3) to COPPER,
            Location(8, 4) to AMBER
        ),
        parseState(largeSample)
    )

    @TestFactory
    fun `can determine the end condition`() = listOf(
        sampleStart to false,
        inProgress to false,
        organized to true
    ).map { (input, expected) -> parseState(input) to expected }
        .mapIndexed { i, (state, expected) ->
            DynamicTest.dynamicTest("Case $i is ${if (!expected) " not" else ""} organized") {
                assertEquals(expected, state.isOrganized(CaveMap()))
            }
        }

    @TestFactory
    fun `can count the steps between spots`() =
        listOf(
            Triple(Location(0, 0), Location(1, 0), 1),
            Triple(Location(0, 0), Location(10, 0), 10),
            Triple(Location(10, 0), Location(0, 0), 10),
            Triple(Location(2, 2), Location(4, 2), 6)
        ).map { (from, to, expected) ->
            DynamicTest.dynamicTest("Moving from $from to $to takes $expected steps") {
                assertEquals(expected, CaveMap().countSteps(from, to))
            }
        }

    @Test
    fun `can move all into the hallway`() = assertEquals(
        setOf(
            Move(Location(2, 1), Location(0, 0)),
            Move(Location(2, 1), Location(1, 0)),
            Move(Location(4, 2), Location(5, 0)),
            Move(Location(4, 2), Location(7, 0)),
            Move(Location(4, 2), Location(9, 0)),
            Move(Location(4, 2), Location(10, 0)),
            Move(Location(8, 1), Location(5, 0)),
            Move(Location(8, 1), Location(7, 0)),
            Move(Location(8, 1), Location(9, 0)),
            Move(Location(8, 1), Location(10, 0)),
        ),
        parseState(inProgress).validMoves(CaveMap())
    )


    @Test
    fun `can move all into a room`() {
        val moves = parseState(emptyDestination).validMoves(CaveMap())
        assertTrue(
            moves.containsAll(
                setOf(
                    Move(Location(1, 0), Location(8, 2)),
                    Move(Location(10, 0), Location(6, 1)),
                )
            )
        )
        assertFalse(moves.contains(Move(Location(1, 0), Location(8, 1))))
    }

    @Test
    fun `can move one into the hall`() = assertEquals(
        setOf(
            Move(Location(2, 1), Location(0, 0)),
            Move(Location(2, 1), Location(1, 0)),
        ), parseState(inProgress).validMovesFrom(Location(2, 1), CaveMap())
    )

    @Test
    fun `cannot move into the hall if blocked`() = assertEquals(
        setOf(), parseState(inProgress).validMovesFrom(Location(2, 2), CaveMap())
    )

    @Test
    fun `cannot move from the hall if the room has a different type of amphipod in it`() = assertEquals(
        setOf(),
        parseState(inProgress).validMovesFrom(Location(3, 0), CaveMap())
    )

    @Test
    fun `move C into room`() = assertEquals(
        setOf(Move(Location(10, 0), Location(6, 1))),
        parseState(emptyDestination).validMovesFrom(Location(10, 0), CaveMap())
    )

    @Test
    fun `move D into room`() = assertEquals(
        setOf(Move(Location(1, 0), Location(8, 2))),
        parseState(emptyDestination).validMovesFrom(Location(1, 0), CaveMap())
    )

    @Test
    fun `calculates the best cost to organize`() = assertEquals(
        12521, bestCostToOrganize(parseState(sampleStart))
    )

    @Test
    fun `can make a move`() = assertEquals(
        parseState(inProgress),
        parseState(sampleStart).applyMove(Move(Location(6, 1), Location(3, 0)))
            .applyMove(Move(Location(4, 1), Location(6, 1)))
    )

    @Test
    fun `calculates the best cost to organize the large cave`() = assertEquals(
        44169, bestCostToOrganize(parseState(largeSample), 4)
    )
}