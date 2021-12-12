package day01

import java.io.File

fun main() {
    val measurements = File("measurements.txt").useLines { it.map { l -> l.toInt() }.toList() }
    println("There are ${countIncreases(measurements)} increases in the data set")
    println("There are ${countIncreasesBy3(measurements)} increases when grouping by 3")
}

fun countIncreases(measurement: List<Int>): Int = measurement.windowed(2).count { (a, b) -> b > a }

fun countIncreasesBy3(measurement: List<Int>): Int = countIncreases(measurement.windowed(3).map { it.sum() })