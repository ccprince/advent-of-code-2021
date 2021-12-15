package day14

import kotlin.test.Test
import kotlin.test.assertEquals

class Day14Tests {

    private val sample = listOf(
        "NNCB",
        "",
        "CH -> B",
        "HH -> N",
        "CB -> H",
        "NH -> C",
        "HB -> C",
        "HC -> B",
        "HN -> C",
        "NN -> C",
        "BH -> H",
        "NC -> B",
        "NB -> B",
        "BN -> B",
        "BB -> N",
        "BC -> B",
        "CC -> N",
        "CN -> C"
    )

    private val template = parseInput(sample).first
    private val rules = parseInput(sample).second

    @Test
    fun `can do a single polymerization by brute force`() =
        assertEquals("NCNBCHB", polymerizeBruteForce(template, rules))

    @Test
    fun `can do all the steps of the sample`() = assertEquals(
        listOf(
            template,
            "NCNBCHB",
            "NBCCNBBBCBHCB",
            "NBBBCNCCNBBNBNBBCHBHHBCHB",
            "NBBNBNBBCCNBCNCCNBBNBBNBBBNBBNBBCBHCBHHNHCBBCBHCB",
        ), (1..4).scan(template) { s, _ -> polymerizeBruteForce(s, rules) }
    )

    @Test
    fun `can calculate the final value after 10 steps`() =
        assertEquals(1588, runPolymerizationBruteForce(template, rules))

    @Test
    fun `can calculate the final value after 10 steps, smarter`() =
        assertEquals(1588, runPolymerization(template, rules))

    @Test
    fun `can calculate the final value after 40 steps, smarter`() =
        assertEquals(2188189693529, runPolymerization(template, rules, 40))
}