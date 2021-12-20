package day19

import cartesianProduct
import java.io.File

fun main() {
    val scannerData = parseToScannerData(File("scanners.txt").readLines())

    val sample0 = setOf(Point3D(0, 2, 0), Point3D(4, 1, 0), Point3D(3, 3, 0))
    val sample1 = setOf(Point3D(-1, -1, 0), Point3D(-5, 0, 0), Point3D(-2, 1, 0))

    println(sample0.intersect(sample1.moveTo(Point3D(5, 2, 0))))
}

fun parseToScannerData(input: List<String>): Array<ScannerData> = input.separateBy("")
    .map { points ->
        points.drop(1).map { p -> Point3D.fromString(p) }.toSet()
    }.toTypedArray()

fun <T> List<T>.separateBy(separator: T): List<List<T>> {
    val indices = listOf(-1) + mapIndexedNotNull { i, x -> if (x == separator) i else null } + listOf(size)
    return indices.windowed(2)
        .map {
            subList(it[0] + 1, it[1])
        }
}

data class Point3D(val x: Int, val y: Int, val z: Int) {

    operator fun plus(other: Point3D): Point3D = Point3D(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Point3D): Point3D = Point3D(x - other.x, y - other.y, z - other.z)

    companion object {
        fun fromString(s: String): Point3D {
            val (x, y, z) = s.split(",").map { it.toInt() }
            return Point3D(x, y, z)
        }
    }
}

typealias ScannerData = Set<Point3D>

fun ScannerData.sharesBeacons(other: ScannerData, min: Int = 12): Boolean {
    return possibleOriginsOf(other).map { other.moveTo(it) }
        .firstOrNull { intersect(it).size >= min } != null
}

fun ScannerData.allOrientations(): Sequence<ScannerData> = TODO()

fun ScannerData.possibleOriginsOf(other: ScannerData): Sequence<Point3D> = cartesianProduct(this, other)
    .asSequence()
    .map { (a, b) -> a - b }

fun ScannerData.moveTo(newOrigin: Point3D): ScannerData = map { it + newOrigin }.toSet()

fun countBeacons(scanners: Array<ScannerData>): Int = TODO()

