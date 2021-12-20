package day20

import kotlin.test.Test
import kotlin.test.assertEquals

class Day20Tests {

    private val sampleData = parseInput(
        listOf(
            "..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#",
            "",
            "#..#.",
            "#....",
            "##..#",
            "..#..",
            "..###",
        )
    )

    private val sampleAlgorithm = sampleData.first
    private val sampleImage = sampleData.second

    @Test
    fun `gets the value at a pixel`() = assertEquals(34, sampleImage.valueAt(2, 2))

    @Test
    fun `can transform an image twice, then count light pixels`() =
        assertEquals(35, sampleImage.transform(sampleAlgorithm, 2).countLightPixels())

    @Test
    fun `can transform an image 50 times, then count light pixels`() =
        assertEquals(3351, sampleImage.transform(sampleAlgorithm, 50).countLightPixels())
}