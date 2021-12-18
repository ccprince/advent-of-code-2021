package day18

import org.junit.jupiter.api.Disabled
import kotlin.test.Test

import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import kotlin.test.assertEquals

class Day18Tests {

    @TestFactory
    fun `can parse and print some numbers`() = listOf(
        "[1,2]",
        "[[1,2],3]",
        "[9,[8,7]]",
        "[[1,9],[8,5]]",
        "[[[[1,2],[3,4]],[[5,6],[7,8]]],9]",
        "[[[9,[3,8]],[[0,9],6]],[[[3,7],[4,9]],3]]",
        "[[[[1,3],[5,3]],[[1,3],[8,7]]],[[[4,9],[6,9]],[[8,2],[7,3]]]]",
    )
        .map { str ->
            DynamicTest.dynamicTest("$str parses and prints correctly") {
                val sn = snailNumberFromString(str)
                assertEquals(str, sn.asString())
            }
        }

    @Test
    fun `sets up the parent-child relationship while parsing`() {

    }

    @TestFactory
    fun `can calculate magnitude`() = listOf(
        "[9,1]" to 29,
        "[1,9]" to 21,
        "[[9,1],[1,9]]" to 129,
        "[[1,2],[[3,4],5]]" to 143,
        "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]" to 1384,
        "[[[[1,1],[2,2]],[3,3]],[4,4]]" to 445,
        "[[[[3,0],[5,3]],[4,4]],[5,5]]" to 791,
        "[[[[5,0],[7,4]],[5,5]],[6,6]]" to 1137,
        "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]" to 3488,
    ).map { (str, expected) ->
        dynamicTest("$str has magnitude $expected") {
            assertEquals(expected, snailNumberFromString(str).magnitude())
        }
    }

    @Test
    fun `can add two numbers`() = assertEquals(
        snailNumberFromString("[[1,2],[[3,4],5]]"),
        snailNumberFromString("[1,2]") + snailNumberFromString("[[3,4],5]")
    )

    @TestFactory
    fun `can split a simple regular number`() = listOf(
        SnailRegular(10) to SnailPair(SnailRegular(5), SnailRegular(5)),
        SnailRegular(11) to SnailPair(SnailRegular(5), SnailRegular(6))
    ).map { (input, expected) ->
        dynamicTest("$input splits to $expected") {
            assertEquals(expected, input.split())
        }
    }

    @Test
    fun `can split a number for real`() = assertEquals(
        snailNumberFromString("[[[[0,7],4],[[7,8],[0,13]]],[1,1]]"),
        snailNumberFromString("[[[[0,7],4],[15,[0,13]]],[1,1]]").split()
    )

    @TestFactory
    fun `can explode a number`() = listOf(
        "[[[[[9,8],1],2],3],4]" to "[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]" to "[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]" to "[[6,[5,[7,0]]],3]",
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
        "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[7,0]]]]",
        "[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]" to "[[[[0,7],4],[7,[[8,4],9]]],[1,1]]",
    ).map { (a, b) -> snailNumberFromString(a) as SnailPair to snailNumberFromString(b) }
        .map { (input, expected) ->
            dynamicTest("${input.asString()} explodes to ${expected.asString()}") {
                assertEquals(expected, input.exploded())
            }
        }

    @Test
    fun `can sum two numbers that need no reducing`() = assertEquals(
        snailNumberFromString("[[1,2],[[3,4],5]]"),
        listOf("[1,2]", "[[3,4],5]").map { snailNumberFromString(it) }.sum()
    )

    @TestFactory
    fun `can sum a list of numbers`() = listOf(
        listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
        ) to "[[[[1,1],[2,2]],[3,3]],[4,4]]",
        listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
            "[5,5]",
        ) to "[[[[3,0],[5,3]],[4,4]],[5,5]]",
        listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]",
            "[5,5]",
            "[6,6]",
        ) to "[[[[5,0],[7,4]],[5,5]],[6,6]]",
        listOf(
            "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
            "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
            "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
            "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
            "[7,[5,[[3,8],[1,4]]]]",
            "[[2,[2,2]],[8,[8,1]]]",
            "[2,9]",
            "[1,[[[9,3],9],[[9,0],[0,7]]]]",
            "[[[5,[7,4]],7],1]",
            "[[[[4,2],2],6],[8,7]]",
        ) to "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
    ).map { (ins, out) -> ins.map { snailNumberFromString(it) } to snailNumberFromString(out) }
        .mapIndexed { idx, (input, expected) ->
            dynamicTest("sum a list of numbers case $idx") {
                assertEquals(expected, input.sum())
            }
        }

    @Test
    fun `can reduce a number`() = assertEquals(
        snailNumberFromString("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"),
        snailNumberFromString("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]").reduced()
    )

    @Test
    fun `can find the largest additive magnitude of a list of numbers`() = assertEquals(
        3993,
        largestAdditiveMagnitude(listOf(
            "[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
            "[[[5,[2,8]],4],[5,[[9,9],0]]]",
            "[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
            "[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
            "[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
            "[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
            "[[[[5,4],[7,7]],8],[[8,3],8]]",
            "[[9,3],[[9,9],[6,[4,9]]]]",
            "[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
            "[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]",
        ).map { snailNumberFromString(it) })
    )
}