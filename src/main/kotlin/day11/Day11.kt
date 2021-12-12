package day11

import day09.parseArray
import java.io.File

fun main() {
    val octopus = parseArray(File("octopus-garden.txt").readLines())
    println("After 100 steps, there were ${countFlashes(octopus, 100)} flashes")
    println("The first synchronized flash happens at step ${firstSynchronizedFlash(octopus)}")
}

fun countFlashes(data: List<List<Int>>, steps: Int): Int {
    var flashCount = 0
    val garden = data.map { row -> IntArray(row.size) { idx -> row[idx] } }
    for (step in 1..steps) {
        val flashes = doStep(garden)
        flashCount += flashes.sumOf { row -> row.count { it } }
    }

    return flashCount
}

fun firstSynchronizedFlash(data: List<List<Int>>): Int {
    val garden = data.map { row -> IntArray(row.size) { idx -> row[idx] } }
    var flashes: List<Array<Boolean>>

    var step = 0
    do {
        step++
        flashes = doStep(garden)
    } while (flashes.any { row -> row.any { !it }})

    return step
}

// This is sleazy, modifying the input like it does...
private fun doStep(garden: List<IntArray>): List<Array<Boolean>> {
    garden.traverse { row, col, n -> garden[row][col] = n + 1 }

    val flashes = List(garden.size) { Array(garden[0].size) { false } }
    while (true) {
        var newFlashes = false
        garden.traverse { row, col, n ->
            if (n > 9 && !flashes[row][col]) {
                flashes[row][col] = true
                newFlashes = true

                for ((r, c) in neighbors(row, col)) {
                    garden[r][c]++
                }
            }
        }

        if (!newFlashes) {
            break
        }
    }

    garden.traverse { row, col, _ ->
        if (flashes[row][col]) {
            garden[row][col] = 0
        }
    }

    return flashes
}

private fun neighbors(row: Int, col: Int): List<Pair<Int,Int>> = (-1..1).flatMap { dRow ->
    (-1..1).mapNotNull { dCol ->
        if (!(dRow == 0 && dCol == 0) && row + dRow in (0..9) && col + dCol in (0..9)) {
            Pair(row + dRow, col + dCol)
        } else
            null
    }
}

typealias Processor = (Int, Int, Int) -> Unit

fun List<IntArray>.traverse(processor: Processor) {
    for (row in indices) {
        for (col in this[row].indices) {
            processor(row, col, this[row][col])
        }
    }
}