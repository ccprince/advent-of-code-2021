package day14

import java.io.File

fun main() {
    val (template, rules) = parseInput(File("rules.txt").readLines())

    println("After polymerization, the value is ${runPolymerizationBruteForce(template, rules)}")
    println()
    println("After more polymerization, the value is ${runPolymerization(template, rules, 40)}")
}

private val ruleRegex = Regex("""(.{2}) -> (.)""")

fun parseInput(data: List<String>): Pair<String, Map<String, String>> = Pair(
    data[0],
    data.drop(2).map {
        val (rule, result) = ruleRegex.matchEntire(it)!!.destructured
        rule to result
    }.toMap()
)


//
// My first, brute-force approach. Great for part 1, not great for part 2
//

fun polymerizeBruteForce(s: String, rules: Map<String, String>): String = s.mapIndexed { i, c ->
    if (i == s.length - 1)
        c
    else {
        val key = s.substring(i, i + 2)
        if (key in rules)
            c + rules.getValue(key)
        else
            c
    }
}.joinToString("")

fun countCharacters(s: String): Map<Char, Int> = s.groupingBy { it }.eachCount()
fun mostFrequent(s: String): Int = countCharacters(s).values.maxOf { it }
fun leastFrequent(s: String): Int = countCharacters(s).values.minOf { it }
fun runPolymerizationBruteForce(template: String, rules: Map<String, String>, steps: Int = 10): Int {
    val polymer = (1..steps).fold(template) { s, _ -> polymerizeBruteForce(s, rules) }
    return mostFrequent(polymer) - leastFrequent(polymer)
}

//
// A smarter approach, inspired by many other people's solutions on Reddit...
//
// Instead of building the entire string, we just keep track of the pairs. Then take each pair, and turn it
// into the same number of pairs of each of the transformations. For instance, the rule "AB -> C" would transform
// a single "AB" into "ACB", which the same as a pair of "AC" and a pair of "AB". If there were three ABs, it would
// become three of each.
//
// Then, to count the frequencies of the letters, we can count the frequencies of the first letter of each pair. We
// also have to add one for the last character of the original input, because it's never part of a pair, and it
// never changes.
//

fun runPolymerization(template: String, rules: Map<String, String>, steps: Int = 10): Long {
    var pairCount = countPairs(template)
    val transformations = rules.mapValues { (k, v) -> listOf(k[0] + v, v + k[1]) }

    repeat(steps) {
        val newCount = HashMap<String, Long>().withDefault { 0 }
        for ((k, v) in pairCount) {
            for (t in transformations.getValue(k)) {
                newCount.put(t, newCount.getValue(t) + v)
            }
        }

        pairCount = newCount
    }

    val frequencies = pairCount.entries.map { (k, v) -> k[0] to v }
        .groupBy({ it.first }, { it.second })
        .mapValues { (k, v) -> v.sum() + if (k == template.last()) 1 else 0 }
    return frequencies.values.maxOf { it } - frequencies.values.minOf { it }
}

fun countPairs(s: String): Map<String, Long> = s.windowed(2)
    .groupBy { it }
    .mapValues { (_, v) -> v.size.toLong() }