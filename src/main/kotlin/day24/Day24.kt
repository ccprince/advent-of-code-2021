package day24

import java.io.File

fun main() {
    // Don't run this ... it's a brute-force approach, which doesn't work. The way to solve this is to analyze the
    // program and figure out something better.
    val program = parseInstructions(File("monad.txt").readLines())
    val serialNumber = findLargestSerialNumber(program)
    println("The largest valid serial number is $serialNumber")
}

data class State(val input: List<Int>, val variable: Map<String, Int> = mapOf()) {
    fun read(): Pair<Int, State> = Pair(input.first(), copy(input = input.drop(1)))
    fun set(v: String, n: Int): State = copy(variable = variable + (v to n))
}
data class Instruction(val type: String, val paramA: String, val paramB: String = "")

fun parseInstructions(input: List<String>): List<Instruction> = input.map { line ->
    val parts = line.split(" ") + listOf("")
    Instruction(parts[0], parts[1], parts[2])
}

fun execute(instruction: Instruction, state: State): State {
    val a = state.variable[instruction.paramA] ?: 0
    val b = when {
        instruction.paramB.isEmpty() -> -999
        instruction.paramB.isNumber() -> instruction.paramB.toInt()
        else -> state.variable[instruction.paramB] ?: 0
    }

    return when (instruction.type) {
        "inp" -> {
            val (input, newState) = state.read()
            newState.set(instruction.paramA, input)
        }
        "add" -> state.set(instruction.paramA, a + b)
        "mul" -> state.set(instruction.paramA, a * b)
        "div" -> state.set(instruction.paramA, a / b)
        "mod" -> state.set(instruction.paramA, a % b)
        "eql" -> state.set(instruction.paramA, if (a == b) 1 else 0)
        else -> throw RuntimeException("Unknown instruction ${instruction.type}")
    }
}

private fun String.isNumber(): Boolean = all { it.isDigit() || it == '-'}

fun run(program: List<Instruction>, input: String): State {
    val initialState = State(input.map { it - '0' })
    return program.fold(initialState) { state, instruction -> execute(instruction, state) }
}

fun findLargestSerialNumber(program: List<Instruction>): Long {
    val seq = generateSequence(99999999999999) { if (it >= 10000000000000) it - 1 else null }
    return seq.filterNot { it.toString().contains('0') }
        .find { serial ->
            val result = run(program, serial.toString())
            result.variable["z"] == 0
        } ?: throw RuntimeException("Didn't find a valid serial number")
}