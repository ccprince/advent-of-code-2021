package day05

import java.io.File
import kotlin.math.sign

fun main() {
    val lines = File("vents.txt").readLines().map { Line.fromString(it) }

    val overlapCount = calculateOverlap(lines.filter { it.isOrthogonal })
    println("There are $overlapCount overlapping orthogonal vents")

    val overlapCount2 = calculateOverlap(lines)
    println("There are $overlapCount2 overlapping vents, including diagonals")
}

data class Line(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {

    val isOrthogonal = x1 == x2 || y1 == y2

    fun toPoints(): List<Pair<Int, Int>> {
        val dx = (x2 - x1).sign
        val dy = (y2 - y1).sign

        var p = Pair(x1, y1)
        val points = mutableListOf(p)
        while (p != Pair(x2, y2)) {
            p = Pair(p.first + dx, p.second + dy)
            points.add(p)
        }

        return points
    }

    companion object {
        private val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
        fun fromString(s: String): Line = regex.matchEntire(s)!!.destructured
            .let { (x1, y1, x2, y2) -> Line(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt()) }
    }
}

fun calculateOverlap(lines: List<Line>): Int {
    val counts = mutableMapOf<Pair<Int, Int>, Int>()
    for (line in lines) {
        for (point in line.toPoints()) {
            counts.put(point, counts.getOrElse(point) { 0 } + 1)
        }
    }

    return counts.values.count { it > 1 }
}

