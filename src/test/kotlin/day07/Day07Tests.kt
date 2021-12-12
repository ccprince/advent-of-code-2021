package day07

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.Test
import kotlin.test.assertEquals

class Day07Tests {

    private val initial = listOf(16,1,2,0,4,2,7,1,2,14)

    @TestFactory
    fun `calculates needed fuel`() = listOf(
        2 to 37,
        1 to 41,
        3 to 39,
        10 to 71
    ).map { (position, fuel) ->
        DynamicTest.dynamicTest("moving to position $position costs $fuel fuel") {
            assertEquals(fuel, initial.fuelToReachPosition(position))
        }
    }

    @Test
    fun `calculates cheapest position`() = assertEquals(2, cheapestPosition(initial))

    @TestFactory
    fun `calculates needed fuel with variable rate`() = listOf(
        5 to 168,
        2 to 206
    ).map { (position, fuel) ->
        DynamicTest.dynamicTest("moving to position $position costs $fuel fuel") {
            assertEquals(fuel, initial.fuelToReachPositionVariableRate(position))
        }
    }

    @Test
    fun `calculates cheapest position with variable rate`() = assertEquals(5, cheapestPositionVariableRate(initial))
}