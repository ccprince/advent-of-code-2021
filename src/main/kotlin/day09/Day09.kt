package day09

import java.io.File

fun main() {
    val depthMap = parseArray(File("depths.txt").readLines())

    println("The total risk on this map is ${totalRisk(depthMap)}")

    val basins = lowSpots(depthMap).map { basinAt(it, depthMap) }
    val topThreeSizes = basins.map { it.size }.sorted().takeLast(3)
    val product = topThreeSizes.reduce { a, b, -> a * b }
    println("The product of the largest three basins is $product")
}

fun parseArray(s: List<String>): List<List<Int>> = s.map { rowString -> rowString.map { c -> c - '0' } }

private val deltas = listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))
fun List<List<Int>>.neighborsOf(x: Int, y: Int): List<Pair<Int,Int>> =
    deltas.map { (dx, dy) -> Pair(x + dx, y + dy) }
        .filter { (u, v) -> u in 0 until this.size && v in 0 until this[0].size }
fun List<List<Int>>.neighborsOf(point: Pair<Int,Int>): List<Pair<Int,Int>> = neighborsOf(point.first, point.second)
operator fun List<List<Int>>.get(point: Pair<Int,Int>): Int = this[point.first][point.second]

fun lowSpots(data: List<List<Int>>): Set<Pair<Int,Int>> {
    val height = data.size
    val width = data[0].size
    return (0 until width).flatMap { y -> (0 until height).map { x -> Pair(x, y) }}
        .filter { p -> data.neighborsOf(p).all { n -> data[n] > data[p] } }
        .toSet()
}

fun totalRisk(data: List<List<Int>>): Int = lowSpots(data).sumOf { (x, y) -> data[x][y] + 1 }

fun basinAt(lowPoint: Pair<Int,Int>, data: List<List<Int>>): Set<Pair<Int,Int>> {
    val result = mutableSetOf<Pair<Int,Int>>()
    val candidates = mutableSetOf(lowPoint)

    while (candidates.isNotEmpty()) {
        val next = candidates.first().also { candidates.remove(it) }
        result.add(next)
        candidates.addAll(data.neighborsOf(next).filter { p -> p !in result && data[p] != 9 })
    }

    return result
}

fun basinAt2(lowPoint: Pair<Int,Int>, data: List<List<Int>>): Set<Pair<Int,Int>> {
    tailrec fun recurse(partial: Set<Pair<Int,Int>>, candidates: Set<Pair<Int,Int>>): Set<Pair<Int,Int>> =
        if (candidates.isEmpty()) {
            partial
        } else {
            val next = candidates.first()
            val nextPartial = partial + setOf(next)
            val nextCandidates = candidates +
                    data.neighborsOf(next).filter { p -> p !in partial && data[p] != 9 }.toSet() -
                    setOf(next)
            recurse(nextPartial, nextCandidates)
        }

    return recurse(setOf(), setOf(lowPoint))
}