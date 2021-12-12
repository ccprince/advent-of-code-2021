package day03

import java.io.File

fun main() {
    val diagnostics = File("diagnostics.txt").readLines()
    val gammaRate = gammaRate(diagnostics)
    val epsilonRate = epsilonRate(diagnostics)

    println("Rates: gamma = $gammaRate, epsilon = $epsilonRate")
    println("Product = ${gammaRate * epsilonRate}")

    val oxygenRating = oxygenGeneratorRating(diagnostics)
    val co2Rating = co2ScrubberRating(diagnostics)

    println("Ratings: oxygen = $oxygenRating, CO2 = $co2Rating")
    println("Product = ${oxygenRating * co2Rating}")
}

fun gammaRate(diagnostics: List<String>): Int = mostCommonBits(diagnostics).joinToString("").toInt(2)
fun epsilonRate(diagnostics: List<String>): Int = leastCommonBits(diagnostics).joinToString("").toInt(2)

fun countOnes(strs: List<String>): List<Int> {
    val counts = MutableList(strs[0].length) { 0 }
    for (s in strs) {
        for ((idx, c) in s.withIndex()) {
            if (c == '1') counts[idx] = counts[idx] + 1
        }
    }

    return counts
}

fun mostCommonBits(strs: List<String>): List<Char> = countOnes(strs).map { if (it > strs.size / 2) '1' else '0' }
fun leastCommonBits(strs: List<String>): List<Char> = countOnes(strs).map { if (it < strs.size / 2) '1' else '0' }

fun oxygenGeneratorRating(diagnostics: List<String>): Int = repeatedFilter(diagnostics) { zeroes, ones ->
    if (zeroes == ones) '1'
    else if (zeroes > ones) '0' else '1'
}.toInt(2)

fun co2ScrubberRating(diagnostics: List<String>): Int = repeatedFilter(diagnostics) { zeroes, ones ->
    if (zeroes == ones) '0'
    else if (zeroes < ones) '0' else '1'
}.toInt(2)

typealias Criterion = (zeroCount: Int, oneCount: Int) -> Char
fun repeatedFilter(strs: List<String>, criterion: Criterion): String {
    var candidates = strs
    var idx = 0
    while (candidates.size > 1) {
        val counts = countOnes(candidates)
        val bitToKeep = criterion(candidates.size - counts[idx], counts[idx])
        candidates = candidates.filter { it[idx] == bitToKeep }
        idx++
    }

    return candidates[0]
}