package day10

import day10.ResultType.*
import java.io.File
import java.util.*

fun main() {
    val program = File("broken-program.txt").readLines()

    println("The progam's syntax-error score is ${syntaxScore(program)}")
    println("The program's auto-complete score is ${autocompleteScore(program)}")
}

fun Char.isOpen(): Boolean = this in setOf('(', '[', '{', '<')

enum class ResultType { VALID, CORRUPTED, INCOMPLETE }
typealias Result = Pair<ResultType, Char?>

fun validate(line: String): Result {
    val stack = Stack<Char>()
    for (c in line) {
        if (c.isOpen())
            stack.push(c)
        else {
            val open = stack.pop()
            if (!matches(open, c))
                return Pair(CORRUPTED, c)
        }
    }

    return Pair(if (stack.isEmpty()) VALID else INCOMPLETE, null)
}

fun matches(open: Char, close: Char): Boolean =
    when (open) {
        '(' -> close == ')'
        '[' -> close == ']'
        '{' -> close == '}'
         else -> close == '>'
    }


fun syntaxScore(program: List<String>): Int = program.map { validate(it) }
    .map { (_, corruptChar) ->
        when (corruptChar) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> 0
        }
    }.sum()

fun completion(line: String): String {
    val stack = Stack<Char>()
    for (c in line) {
        if (c.isOpen())
            stack.push(c)
        else {
            stack.pop()
        }
    }

    return stack.reversed()
        .map { when (it) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            else -> '>'
        } }.joinToString("")
}

fun autocompleteScore(program: List<String>): Long {
    val sortedScores = program.filter { validate(it).first == INCOMPLETE }
        .map { autocompleteScore(it) }.sorted()
    return sortedScores.drop(sortedScores.size / 2).first()
}

fun autocompleteScore(line: String): Long = completion(line)
    .fold(0) { acc, c ->
    acc * 5 + when (c) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        else -> 4
    }
}