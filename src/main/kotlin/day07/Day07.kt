package day07

import java.io.File
import kotlin.math.abs


fun main() {
    val crabs = File("crabs.txt").readLines()[0].split(",").map { it.toInt() }
    val position = cheapestPosition(crabs)
    val fuel = crabs.fuelToReachPosition(position)
    println("The best position is $position, which costs $fuel fuel")

    val variablePosition = cheapestPositionVariableRate(crabs)
    val variableFuel = crabs.fuelToReachPositionVariableRate(variablePosition)
    println("The best variable-rate position is $variablePosition, which costs $variableFuel fuel")

    val emptyList = listOf<Int>()
    val nullList: List<Int>? = null
}

fun List<Int>.fuelToReachPosition(pos: Int): Int = sumOf { abs(it - pos) }
fun cheapestPosition(initialPositions: List<Int>): Int = initialPositions.rangeOf()
    .minByOrNull { initialPositions.fuelToReachPosition(it) } ?: throw IllegalArgumentException("No empty lists")

fun List<Int>.fuelToReachPositionVariableRate(pos: Int): Int = sumOf { stepCost(abs(it - pos)) }
fun cheapestPositionVariableRate(initialPositions: List<Int>): Int = initialPositions.rangeOf()
    .minByOrNull { initialPositions.fuelToReachPositionVariableRate(it) } ?: throw IllegalArgumentException("No empty lists")

//
// This memoization _does_ vastly speed up the calculation, but it still only takes a couple seconds.
//private val savedStepCosts = mutableMapOf<Int, Int>()
//fun stepCost(distance: Int): Int = if (distance in savedStepCosts) savedStepCosts[distance]!!
//else {
//    val cost = (1..distance).sum()
//    savedStepCosts.put(distance, cost)
//    cost
//}

fun stepCost(distance: Int): Int = (1..distance).sum()

private fun List<Int>.rangeOf(): IntRange = minOf { it }..maxOf { it }