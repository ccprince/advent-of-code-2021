package day25

import java.io.File

fun main() {
    val input = File("seafloor.txt").readLines()
    val (_, _, steps) = moveCucumbers(input)
    println("The cucumbers are all blocked after $steps steps")
}

typealias Point = Pair<Int, Int>
typealias Herd = Set<Point>

fun findHerd(cucumber: Char, data: List<String>): Set<Point> = data.flatMapIndexed { y, row ->
    row.mapIndexedNotNull { x, c -> if (c == cucumber) Point(x, y) else null }
}.toSet()

fun moveCucumbers(input: List<String>, endAtStep: Int? = null): Triple<Herd, Herd, Int> {
    var eastHerd = findHerd('>', input)
    var southHerd = findHerd('v', input)
    val width = input[0].length
    val height = input.size


    var count = 0
    while (endAtStep == null || count < endAtStep) {
        var somethingMoved = false

        fun Herd.moveHerd(direction: Point): Herd = map {
            val destination = Point((it.first + direction.first) % width, (it.second + direction.second) % height)
            if (!eastHerd.contains(destination) && !southHerd.contains(destination)) {
                somethingMoved = true
                destination
            } else
                it
        }.toSet()

        count++
        eastHerd = eastHerd.moveHerd(Point(1, 0))
        southHerd = southHerd.moveHerd(Point(0, 1))

        if (!somethingMoved) break
    }

    return Triple(eastHerd, southHerd, count)
}