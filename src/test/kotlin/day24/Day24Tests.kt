package day24

import kotlin.test.Test
import kotlin.test.assertEquals

class Day24Tests {

    @Test
    fun `can execute an INP instruction`() = assertEquals(
        State(listOf(), mapOf("x" to 8)),
        execute(Instruction("inp", "x"), State(listOf(8)))
    )

    @Test
    fun `can execute an ADD with two registers`() = assertEquals(
        State(listOf(), mapOf("a" to 4, "b" to 3)),
        execute(Instruction("add", "a", "b"), State(listOf(), mapOf("a" to 1, "b" to 3)))
    )

    @Test
    fun `can execute an ADD with a constant`() = assertEquals(
        State(listOf(), mapOf("a" to 42)),
        execute(Instruction("add", "a", "4"), State(listOf(), mapOf("a" to 38)))
    )

    @Test
    fun `can execute a MUL with two variables`() = assertEquals(
        State(listOf(), mapOf("a" to 12, "b" to 3)),
        execute(Instruction("mul", "a", "b"), State(listOf(), mapOf("a" to 4, "b" to 3)))
    )

    @Test
    fun `can execute a MUL with a constant`() = assertEquals(
        State(listOf(), mapOf("a" to -42)),
        execute(Instruction("mul", "a", "-7"), State(listOf(), mapOf("a" to 6)))
    )

    @Test
    fun `can execute a DIV with two variables`() = assertEquals(
        State(listOf(), mapOf("a" to 5, "b" to 3)),
        execute(Instruction("div", "a", "b"), State(listOf(), mapOf("a" to 16, "b" to 3)))
    )

    @Test
    fun `can execute a DIV with a constant`() = assertEquals(
        State(listOf(), mapOf("a" to -4)),
        execute(Instruction("div", "a", "2"), State(listOf(), mapOf("a" to -9)))
    )

    @Test
    fun `can execute a MOD with two variables`() = assertEquals(
        State(listOf(), mapOf("a" to 2, "b" to 3)),
        execute(Instruction("mod", "a", "b"), State(listOf(), mapOf("a" to 5, "b" to 3)))
    )

    @Test
    fun `can execute a MOD with a constant`() = assertEquals(
        State(listOf(), mapOf("a" to 2)),
        execute(Instruction("mod", "a", "3"), State(listOf(), mapOf("a" to 5)))
    )

    @Test
    fun `can execute an EQL with two variables`() = assertEquals(
        State(listOf(), mapOf("a" to 1, "b" to 42)),
        execute(Instruction("eql", "a", "b"), State(listOf(), mapOf("a" to 42, "b" to 42)))
    )

    @Test
    fun `can execute an EQL with a constant`() = assertEquals(
        State(listOf(), mapOf("a" to 0)),
        execute(Instruction("eql", "a", "99"), State(listOf(), mapOf("a" to 42)))
    )

    @Test
    fun `can run a binary-conversion program`() {
        val program = parseInstructions(
            listOf(
                "inp w",
                "add z w",
                "mod z 2",
                "div w 2",
                "add y w",
                "mod y 2",
                "div w 2",
                "add x w",
                "mod x 2",
                "div w 2",
                "mod w 2",
            )
        )
        assertEquals(
            State(listOf(), mapOf("w" to 1, "x" to 0, "y" to 0, "z" to 1)),
            run(program, "9")
        )
    }
}