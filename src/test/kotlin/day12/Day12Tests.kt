package day12

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class Day12Tests {

    private val smallSample = buildCaveMap(
        listOf(
            "start-A",
            "start-b",
            "A-c",
            "A-b",
            "b-d",
            "A-end",
            "b-end",
        )
    )

    private val mediumSample = buildCaveMap(
        listOf(
            "dc-end",
            "HN-start",
            "start-kj",
            "dc-start",
            "dc-HN",
            "LN-dc",
            "HN-end",
            "kj-sa",
            "kj-HN",
            "kj-dc",
        )
    )

    private val largeSample = buildCaveMap(
        listOf(
            "fs-end",
            "he-DX",
            "fs-he",
            "start-DX",
            "pj-DX",
            "end-zg",
            "zg-sl",
            "zg-pj",
            "pj-he",
            "RW-he",
            "fs-DX",
            "pj-RW",
            "zg-RW",
            "start-pj",
            "he-WI",
            "zg-he",
            "pj-fs",
            "start-RW",
        )
    )

    @TestFactory
    fun `can count paths`() = listOf(
        smallSample to 10,
        mediumSample to 19,
        largeSample to 226
    ).map { (caves, expected) ->
        dynamicTest("cave with ${caves.size} connections has ${expected} paths") {
            assertEquals(expected, countPaths(caves))
        }
    }

    @Test
    fun `can build a map of connections between caves`() = assertEquals(
        mapOf(
            Cave("start") to setOf(Cave("A"), Cave("b")),
            Cave("c") to setOf(Cave("A")),
            Cave("A") to setOf(Cave("start"), Cave("c"), Cave("b"), Cave("end")),
            Cave("b") to setOf(Cave("A"), Cave("start"), Cave("d"), Cave("end")),
            Cave("d") to setOf(Cave("b")),
            Cave("end") to setOf(Cave("A"), Cave("b"))
        ), smallSample
    )

    @TestFactory
    fun `can count longer paths`() = listOf(
        smallSample to 36,
        mediumSample to 103,
        largeSample to 3509
    ).map { (caves, expected) ->
        dynamicTest("cave with ${caves.size} connections has ${expected} longer paths") {
            assertEquals(expected, countLongerPaths(caves))
        }
    }
}
