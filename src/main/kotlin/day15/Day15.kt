package day15

import java.io.File
import java.util.*

fun main() {
    val data = parseCavern(File("cavern.txt").readLines())

    val simpleCavern = SimpleCavern(data)
    val path = leastRiskyPath(simpleCavern)
    val totalRisk = riskOf(path, simpleCavern)
    println("The total risk of the best path is $totalRisk")

    val expandedCavern = ExpandedCavern(data)
    val expandedPath = leastRiskyPath(expandedCavern)
    val expandedRisk = riskOf(expandedPath, expandedCavern)
    println("The total risk of the best path in the larger cavern is $expandedRisk")
}

fun parseCavern(data: List<String>): List<List<Int>> = data.map { line -> line.map { it - '0' } }


abstract class Cavern(val risks: List<List<Int>>) {
    abstract val width: Int
    abstract val height: Int
    fun riskAt(p: Pair<Int, Int>): Int = p.let { (x, y) -> riskAt(x, y) }
    abstract fun riskAt(x: Int, y: Int): Int

    fun neighbors(p: Pair<Int, Int>): List<Pair<Int, Int>> = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
        .map { (dx, dy) -> Pair(p.first + dx, p.second + dy) }
        .filter { (x, y) -> y in 0 until height && x in 0 until width }

    fun riskOfPath(path: List<Pair<Int, Int>>): Int = path.fold(0) { risk, p -> riskAt(p) + risk } - riskAt(Pair(0, 0))
}

class SimpleCavern(risks: List<List<Int>>) : Cavern(risks) {
    override val width = risks[0].size
    override val height = risks.size
    override fun riskAt(x: Int, y: Int): Int = risks[y][x]
}

class ExpandedCavern(risks: List<List<Int>>) : Cavern(risks) {
    private val singleWidth = risks[0].size
    private val singleHeight = risks.size

    override val width = singleWidth * 5
    override val height = singleHeight * 5

    override fun riskAt(x: Int, y: Int): Int =
        if (x >= singleWidth)
            wrap(riskAt(x - singleWidth, y) + 1)
        else if (y >= singleHeight)
            wrap(riskAt(x, y - singleHeight) + 1)
        else
            risks[y][x]
}

private fun wrap(x: Int): Int = if (x > 9) 1 else x

private data class Node(val point: Pair<Int, Int>, val risk: Int, val previous: Node?)

fun leastRiskyPath(cavern: Cavern): List<Pair<Int, Int>> {
    val visited = mutableSetOf<Pair<Int, Int>>()
    val q = PriorityQueue<Node>(compareBy { it.risk })
    val end = Pair(cavern.width - 1, cavern.height - 1)

    q.add(Node(Pair(0, 0), 0, null))

    while (q.isNotEmpty()) {
        val p = q.poll()

        if (p.point == end) {
            return pathFrom(p)
        }

        if (p.point !in visited) { // Future self: Lack of this condition caused giant performance problems
            visited.add(p.point)
            cavern.neighbors(p.point)
                .forEach {
                    q.offer(Node(it, p.risk + cavern.riskAt(it), p))
                }
        }
    }

    throw RuntimeException("Never found the end point")
}

private fun pathFrom(n: Node): List<Pair<Int, Int>> {
    val result = mutableListOf(n.point)
    var current = n.previous
    while (current != null) {
        result.add(0, current.point)
        current = current.previous
    }
    return result.toList()
}

fun riskOf(path: List<Pair<Int, Int>>, cavern: Cavern): Int =
    path.fold(0) { risk, p -> cavern.riskAt(p) + risk } - cavern.riskAt(Pair(0, 0))