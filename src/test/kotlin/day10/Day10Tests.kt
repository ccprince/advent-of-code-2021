package day10

import day10.ResultType.CORRUPTED
import day10.ResultType.INCOMPLETE
import kotlin.test.Test
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class Day10Tests {

    private val sample = listOf(
        "[({(<(())[]>[[{[]{<()<>>",
        "[(()[<>])]({[<{<<[]>>(",
        "{([(<{}[<>[]}>{[]{[(<()>",
        "(((({<>}<{<{<>}{[]{[]{}",
        "[[<[([]))<([[{}[[()]]]",
        "[{[{({}]{}}([{[{{{}}([]",
        "{<[[]]>}<{[{[{[]{()[[[]",
        "[<(<(<(<{}))><([]([]()",
        "<{([([[(<>()){}]>(<<{{",
        "<{([{{}}[<[[[<>{}]]]>[]]",
    )

    @TestFactory
    fun `validates complete chunks`() = listOf(
        "([])", "{()()()}", "<([{}])>", "[<>({}){}[([])<>]]", "(((((((((())))))))))"
    ).map {
        dynamicTest("$it is VALID") {
            assertEquals(Pair(ResultType.VALID, null), validate(it))
        }
    }

    @TestFactory
    fun `validates broken chunks`() = sample.zip(
        listOf(
            Pair(INCOMPLETE, null),
            Pair(INCOMPLETE, null),
            Pair(CORRUPTED, '}'),
            Pair(INCOMPLETE, null),
            Pair(CORRUPTED, ')'),
            Pair(CORRUPTED, ']'),
            Pair(INCOMPLETE, null),
            Pair(CORRUPTED, ')'),
            Pair(CORRUPTED, '>'),
            Pair(INCOMPLETE, null)
        )
    ).map { (chunk, expected) ->
        dynamicTest("$chunk is ${expected.first}") {
            assertEquals(expected, validate(chunk))
        }
    }

    @Test
    fun `scores a whole program for corrupted lines`() = assertEquals(26397, syntaxScore(sample))

    @TestFactory
    fun `determines how to close incomplete chunks`() = sample.filter { validate(it).first == INCOMPLETE }
        .zip(
            listOf(
                "}}]])})]",
                ")}>]})",
                "}}>}>))))",
                "]]}}]}]}>",
                "])}>",
            )
        ).map { (input, expected) ->
            dynamicTest("$input can be completed with $expected") {
                assertEquals(expected, completion(input))
            }
        }

    @TestFactory
    fun `calculates individual auto-complete scores`() = sample.filter { validate(it).first == INCOMPLETE }
        .zip(listOf<Long>(288957, 5566, 1480781, 995444, 294))
        .map { (input, expected) ->
            dynamicTest("correctly scores $input") {
                assertEquals(expected, autocompleteScore(input))
            }
        }

    @Test
    fun `scores a whole program for auto-completed lines`() = assertEquals(288957, autocompleteScore(sample))
}
