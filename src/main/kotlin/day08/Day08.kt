package day08

import java.io.File

fun main() {
    val observations = File("observations.txt").readLines().map { DisplayObservation.fromString(it) }

    println("There are ${countSimpleDigitsInOutput(observations)} simple digits in the output")
    println("The sum of all the outputs is ${observations.sumOf { it.outputValue }}")
}

data class DisplayObservation(val signalPatterns: List<String>, val outputPatterns: List<String>) {

    val digitSignals = buildMap(10) {
        // Some digits have a unique number of segments
        val patternFor1 = signalPatterns.find { it.length == 2 }!!.toSet()
        val patternFor7 = signalPatterns.find { it.length == 3 }!!.toSet()
        val patternFor4 = signalPatterns.find { it.length == 4 }!!.toSet()
        val patternFor8 = signalPatterns.find { it.length == 7 }!!.toSet()

        put(patternFor1, '1')
        put(patternFor4, '4')
        put(patternFor7, '7')
        put(patternFor8, '8')

        // Six-segment digits:
        // '9' is the only one with all the segments of '4'
        // Of '0' and '6', only '0' has all the segments of '1'
        val sixSegmentPatterns = signalPatterns.filter { it.length == 6 }.map { it.toSet() }.toSet()
        val patternFor9 = sixSegmentPatterns.find { it.containsAll(patternFor4) }!!
        val patternFor0 = (sixSegmentPatterns - setOf(patternFor9)).find { it.containsAll(patternFor1) }!!
        val patternFor6 = (sixSegmentPatterns - setOf(patternFor0, patternFor9)).first()

        put(patternFor0, '0')
        put(patternFor6, '6')
        put(patternFor9, '9')

        // Five-segment digits:
        // '3' is the only one with all the segments of '1'
        // Of '2' and '5', '5' is the one whose segments are all in '6'
        val fiveSegmentPatterns = signalPatterns.filter { it.length == 5 }.map { it.toSet() }.toSet()
        val patternFor3 = fiveSegmentPatterns.find { it.containsAll(patternFor1) }!!
        val patternFor5 = (fiveSegmentPatterns - setOf(patternFor3)).find { it -> patternFor6.containsAll(it) }!!
        val patternFor2 = (fiveSegmentPatterns - setOf(patternFor3, patternFor5)).first()

        put(patternFor2, '2')
        put(patternFor3, '3')
        put(patternFor5, '5')
    }

    val outputValue = outputPatterns.map { digitSignals[it.toSet()] }.joinToString("").toInt()

    companion object {
        fun fromString(s: String): DisplayObservation {
            val sections = s.trim().split(" | ")
            return DisplayObservation(sections[0].split(" "), sections[1].split(" "))
        }
    }
}

fun countSimpleDigitsInOutput(observations: List<DisplayObservation>): Int = observations
    .flatMap { it.outputPatterns }
    .map { it.length }
    .count { it in listOf(2, 3, 4, 7) }

