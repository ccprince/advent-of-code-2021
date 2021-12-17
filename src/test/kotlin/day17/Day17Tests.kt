package day17

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class Day17Tests {

    private data class PathTestData(val dx: Int, val dy: Int, val points: List<Point>)

    private val testData = listOf(
        PathTestData(
            7,
            2,
            listOf(
                Point(0, 0),
                Point(7, 2),
                Point(13, 3),
                Point(18, 3),
                Point(22, 2),
                Point(25, 0),
                Point(27, -3),
                Point(28, -7)
            )
        ),
        PathTestData(
            6,
            3,
            listOf(
                Point(0, 0),
                Point(6, 3),
                Point(11, 5),
                Point(15, 6),
                Point(18, 6),
                Point(20, 5),
                Point(21, 3),
                Point(21, 0),
                Point(21, -4),
                Point(21, -9)
            )
        ),
        PathTestData(
            9,
            0,
            listOf(Point(0, 0), Point(9, 0), Point(17, -1), Point(24, -3), Point(30, -6))
        ),
        PathTestData(
            17,
            -4,
            listOf(Point(0, 0), Point(17, -4), Point(33, -9), Point(48, -15), Point(62, -22))
        )
    )

    @TestFactory
    fun `can calculate a probe's path`() = testData.map { (dx, dy, points) ->
        DynamicTest.dynamicTest("initial velocities ($dx, $dy) ends at ${points.last()}") {
            assertEquals(points, generatePath(dx, dy).take(points.size).toList())
        }
    }

    @Test
    fun `calculates the highest point of a bunch of paths`() = assertEquals(6, maxHeight(testData.map { it.points }))

    private val sampleTarget = Target(xRange = 20..30, yRange = -10..-5)

    @Test
    fun `gets the height of the flashiest launch`() = assertEquals(45, sampleTarget.flashiestHeight())

    @TestFactory
    fun `determines if a path hits the target`() = testData.map { pathToTarget(it.dx, it.dy, sampleTarget) }
        .zip(listOf(true, true, true, false))
        .mapIndexed { idx, (path, expected) ->
            DynamicTest.dynamicTest("Path $idx ${if (expected) "hits" else "misses"} the target") {
                assertEquals(expected, path.hits(sampleTarget))
            }
        }

    @Test
    fun `finds all the combinations that hit the target`() = assertEquals(112, sampleTarget.findAccuratePaths().size)
}
