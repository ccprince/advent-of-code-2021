package day17

import kotlin.math.abs
import kotlin.math.sign

fun main() {
    val target = Target(xRange = 236..262, yRange = -78..-58)
    println("The flashiest height is ${flashiestHeight(target)}")
}

typealias Point = Pair<Int, Int>

fun Point.isInside(target: Target): Boolean = let { (x, y) -> x in target.xRange && y in target.yRange }
fun Point.isPast(target: Target): Boolean = let { (x, y) -> y < target.yRange.first || x > target.xRange.last }

typealias Path = List<Point>

fun Path.hits(target: Target): Boolean = any { it.isInside(target) }

data class Step(val x: Int, val y: Int, val dx: Int, val dy: Int)

data class Target(val xRange: IntRange, val yRange: IntRange)

fun Target.isValidDx(dx: Int): Boolean = generateSequence(Pair(0, dx)) { (x, dx) -> Pair(x + dx, dx - dx.sign) }
    .takeWhile { (_, dx) -> dx > 0 }
    .map { (x, _) -> x }
    .takeWhile { n -> n <= xRange.last }
    .any { it in xRange }

fun Target.validDxs(): List<Int> = (1..(xRange.last)).filter { isValidDx(it) }

fun generatePath(dx: Int, dy: Int): Sequence<Point> = generateSequence(Step(0, 0, dx, dy)) { (x, y, dx, dy) ->
    Step(x + dx, y + dy, dx - dx.sign, dy - 1)
}.map { Point(it.x, it.y) }

fun pathToTarget(dx: Int, dy: Int, target: Target): List<Point> =
    generatePath(dx, dy).takeWhile { !it.isPast(target) }.toList()


//
// Assumptions:
// * dx will always be positive -- The sample target is in that direction
// * dy will always be positive -- The way ballistics works, firing up gets you higher than firing down. Besides,
//   the max height with non-positive dy will always be 0.
// * The far edge of the target is an upper bound for dx, because the second step is guaranteed to be
//   past the target. In fact, that's certainly _way_ above the best dx, but I'm not sure how to pick a lower value.
//
// There are values of dx that will never hit the target. We can eliminate those right away.
//
// Limiting the dy values is the tricky part. Some values of dx will never hit the target, so both take(hits) and
// drop(!hits) will loop forever. Since all upward trajectories will hit y = 0, any probe whose dy is greater than the
// depth of the target is guaranteed to miss it. At y = 0, the probe's dy will be at least as fast as its initial dy.
// So, the depth of the target is an upper bound for dy.
//
fun findAccuratePaths(target: Target): List<Path> = target.validDxs().flatMap { dx ->
    generateSequence(0) { it + 1 }
        .takeWhile { it <= abs(target.yRange.minOf { it })}
        .map { dy -> pathToTarget(dx, dy, target) }
        .filter { it.hits(target) }
        .toList()
}

fun flashiestHeight(target: Target): Int = maxHeight(findAccuratePaths(target))
fun maxHeight(paths: List<Path>): Int = paths.maxOf { path -> path.maxOf { (_, y) -> y } }
