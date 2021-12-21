package day21

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day21Tests {

    @Test
    fun `plays a practice game of Dirac`() = assertEquals(Triple(1000, 745, 993), playDirac(4, 8))

    @Test
    fun `plays one round of quantum Dirac`() {
        val result = DiracState(4, 8, maxScore = 21).playQuantumTurn(3)
        assertEquals(7, result.size)
        assertTrue(
            result.containsAll(
                listOf(
                    DiracState(7, 8, 7, 0, 2, 21) to 3,
                    DiracState(8, 8, 8, 0, 2, 21) to 9,
                    DiracState(9, 8, 9, 0, 2, 21) to 18,
                    DiracState(10, 8, 10, 0, 2, 21) to 21,
                    DiracState(1, 8, 1, 0, 2, 21) to 18,
                    DiracState(2, 8, 2, 0, 2, 21) to 9,
                    DiracState(3, 8, 3, 0, 2, 21) to 3,
                )
            )
        )
    }

    @Test
    fun `plays a practice game of quantum Dirac`() = assertEquals(
        Pair(444356092776315, 341960390180808),
        playQuantumDirac(4, 8)
    )
}