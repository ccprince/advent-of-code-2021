package day06

import java.math.BigInteger
import kotlin.test.Test
import kotlin.test.assertEquals

class Day06Tests {

    private val initial = listOf(3, 4, 3, 1, 2)

    @Test
    fun `simulates a school of fish`() = assertEquals(26.toBigInteger(), simulateFish(initial, 18))

    @Test
    fun `simulates a big school of fish`() = assertEquals(5934.toBigInteger(), simulateFish(initial, 80))

    @Test
    fun `simulates a giant school of fish`() = assertEquals(BigInteger("26984457539"), simulateFish(initial, 256))


//    @Test
//    fun `simulates a school of fish`() = assertEquals(
//        listOf(6, 0, 6, 4, 5, 6, 0, 1, 1, 2, 6, 0, 1, 1, 1, 2, 2, 3, 3, 4, 6, 7, 8, 8, 8, 8),
//        simulateFish(listOf(3, 4, 3, 1, 2), 18)
//    )
//
//    @Test
//    fun `simulates a big school of fish`() = assertEquals(5934, simulateFish(listOf(3, 4, 3, 1, 2), 80).size)

}