package day13

import java.io.File

fun main() {
    val (paper, folds) = parseInput(File("paper.txt").readLines())

    val foldedOnce = paper.doFolds(folds.take(1))
    println("The paper has ${foldedOnce.size} visible dots")

    val folded = paper.doFolds(folds)
    println()
    println("The message is:")
    println(folded.printable())
}


fun parseInput(data: List<String>): Pair<Paper,List<Fold>> {
    val foldRegex = Regex("""fold along ([xy])=(\d+)""")
    val dots = data.takeWhile { it.isNotBlank() }
        .map {
            val parts = it.split(",")
            Pair(parts[0].toInt(), parts[1].toInt())
        }.toSet()
    val folds = data.drop(dots.size + 1).map {
        val (axis, location) = foldRegex.matchEntire(it)!!.destructured
        Fold(axis[0], location.toInt())
    }

    return Pair(dots, folds)
}

typealias Paper = Set<Pair<Int,Int>>

data class Fold(val axis: Char, val location: Int)

fun Paper.foldPaperUp(yAxis: Int): Paper = map { (x, y) ->
    if (y < yAxis) Pair(x, y) else Pair(x, yAxis - (y - yAxis))
}.toSet()

fun Paper.foldPaperLeft(xAxis: Int): Paper = map { (x, y) ->
    if (x < xAxis) Pair(x, y) else Pair(xAxis - (x - xAxis), y)
}.toSet()

fun Paper.doFolds(folds: List<Fold>): Paper = folds.fold(this) { p, f ->
    if (f.axis == 'x') p.foldPaperLeft(f.location) else p.foldPaperUp(f.location)
}

fun Paper.printable(): String {
    val xMax = maxOf { it.first }
    val yMax = maxOf { it.second }
    val str = StringBuilder()
    for (y in 0..yMax) {
        for (x in 0..xMax) {
            str.append(if (contains(Pair(x, y))) "#" else ".")
        }
        str.appendLine()
    }
    return str.toString()
}