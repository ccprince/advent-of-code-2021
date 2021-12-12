package day06

import java.io.File
import java.math.BigInteger

fun main() {
    val initialState = File("fish.txt").readLines()[0].trim().split(",").map { it.toInt() }

    println("After 80 days, there are ${simulateFish(initialState, 80)} lanternfish")
    println("After 256 days, there are ${simulateFish(initialState, 256)} lanternfish")
}

fun simulateFish(initial: List<Int>, days: Int): BigInteger {
    var state = List(9) { initial.count { n -> n == it }.toBigInteger() }
    for (x in 1..days) {
        state = List(9) { n ->
            when (n) {
                8 -> state[0]
                6 -> state[7] + state[0]
                else -> state[n+1]
            }
        }
    }

    return state.sumOf { it }
}

// The naive solution. It works great for the first part, but the second part is too huge, even in the small sample.
//fun simulateFish(initial: List<Int>, days: Int): List<Int> = (1..days).fold(initial) { acc, _ -> step(acc) }
//fun step(state: List<Int>): List<Int> =
//    state.map { if (it == 0) 6 else it - 1 } + List(state.count { it == 0 }) { 8 }
