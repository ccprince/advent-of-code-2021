package day04

import java.io.File

fun main() {
    val input = File("bingo.txt").readLines().filter { it.isNotBlank() }
    val draws = input[0].split(",").map { it.toInt() }
    val cards = input.drop(1)
        .map { it.trim().split(Regex(" +")).map { nums -> nums.toInt() } }
        .chunked(5)
        .map { BingoCard(it) }

    println("The first winning card gives us ${valueOfFirstWinningCard(cards, draws)}")
    println("The last winning card gives us ${valueOfLastWinningCard(cards, draws)}")
}

typealias Spot = Pair<Int, Int>

data class BingoCard(
    val numbers: List<List<Int>>,
    val marks: Set<Spot> = setOf()
)


fun markCard(card: BingoCard, number: Int): BingoCard {
    val row = card.numbers.indexOfFirst { it.contains(number) }
    if (row < 0) return card
    val col = card.numbers.get(row).indexOf(number)
    return card.copy(marks = card.marks + Pair(row, col))
}

fun markDraws(card: BingoCard, draws: List<Int>): BingoCard = draws.fold(card) { c, n -> markCard(c, n) }

fun winningSequence(card: BingoCard, draws: List<Int>): List<Int> {
    var i = 0
    var c = card
    do {
        c = markCard(c, draws[i])
        i++
    } while (!c.isWinner())
    return draws.take(i)
}

fun BingoCard.printed(): String = this.numbers.map { row ->
    row.map { n -> String.format("%2d", n) }
        .joinToString(" ")
}.joinToString("\n")

val WIN_GROUPS = (0..4).map { i -> (0..4).map { j -> Pair(i, j) } } + (0..4).map { i -> (0..4).map { j -> Pair(j, i) } }
fun BingoCard.isWinner(): Boolean = WIN_GROUPS.any { marks.containsAll(it) }

fun BingoCard.score(): Int = numbers.flatMapIndexed { rowIdx, row ->
    row.mapIndexed { colIdx, n ->
        if (!marks.contains(Pair(rowIdx, colIdx))) n else 0
    }
}.sum()

fun valueOfFirstWinningCard(cards: List<BingoCard>, draws: List<Int>): Int {
    val firstWinner = cards.minByOrNull { winningSequence(it, draws).size } ?: throw Exception("Shouldn't happen...")
    val s = winningSequence(firstWinner, draws)
    return s.last() * markDraws(firstWinner, s).score()
}

fun valueOfLastWinningCard(cards: List<BingoCard>, draws: List<Int>): Int {
    val lastWinner = cards.maxByOrNull { winningSequence(it, draws).size } ?: throw Exception("Shouldn't happen...")
    val s = winningSequence(lastWinner, draws)
    return s.last() * markDraws(lastWinner, s).score()
}