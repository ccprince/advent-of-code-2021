package day08

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day08Tests {

    private val singleSample = DisplayObservation(
        listOf("acedgfb", "cdfbe", "gcdfa", "fbcad", "dab", "cefabd", "cdfgeb", "eafb", "cagedb", "ab"),
        listOf("cdfeb", "fcadb", "cdfeb", "cdbaf")
    )

    private val sample = listOf(
        "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe",
        "edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc",
        "fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg",
        "fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb",
        "aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea",
        "fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb",
        "dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe",
        "bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef",
        "egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb",
        "gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce",
    ).map { DisplayObservation.fromString(it) }

    @Test
    fun `can convert from string`() = assertEquals(
        singleSample,
        DisplayObservation.fromString("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")
    )

    @Test
    fun `can count simple digits`() = assertEquals(26, countSimpleDigitsInOutput(sample))

    @Test
    fun `can determine signal-to-digit mapping`() = assertEquals(
        mapOf(
            setOf('a', 'c', 'e', 'd', 'g', 'f', 'b') to '8',
            setOf('c', 'd', 'f', 'b', 'e') to '5',
            setOf('g', 'c', 'd', 'f', 'a') to '2',
            setOf('f', 'b', 'c', 'a', 'd') to '3',
            setOf('d', 'a', 'b') to '7',
            setOf('c', 'e', 'f', 'a', 'b', 'd') to '9',
            setOf('c', 'd', 'f', 'g', 'e', 'b') to '6',
            setOf('e', 'a', 'f', 'b') to '4',
            setOf('c', 'a', 'g', 'e', 'd', 'b') to '0',
            setOf('a', 'b') to '1'
        ),
        singleSample.digitSignals
    )

    @Test
    fun `can decode the output pattern`() = assertEquals(5353, singleSample.outputValue)

    @Test
    fun `can decode a bunch of output patterns`() = assertEquals(
        listOf(8394, 9781, 1197, 9361, 4873, 8418, 4548, 1625, 8717, 4315),
        sample.map { it.outputValue }
    )
}
