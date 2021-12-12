package day02b

import kotlin.test.Test
import kotlin.test.assertEquals

class Day02bTests {

    private val commands = listOf(
        "forward 5",
        "down 5",
        "forward 8",
        "up 3",
        "down 8",
        "forward 2"
    )

    @Test
    fun `sub can move forward`() = assertEquals(Sub(5, 2), Sub(2, 2).move("forward", 3))

    @Test
    fun `sub can move up`() = assertEquals(Sub(5, 2), Sub(5, 4).move("up", 2))

    @Test
    fun `sub can move down`() = assertEquals(Sub(5, 2), Sub(5, 0).move("down", 2))

    @Test
    fun `sub can follow orders`() = assertEquals(Sub(15, 10), Sub(0, 0).followOrders(commands))

    @Test
    fun `aimed sub can move aim down`() = assertEquals(AimedSub(5, 0, 5), AimedSub(5, 0, 0).move("down", 5))

    @Test
    fun `aimed sub can move forward`() = assertEquals(AimedSub(13, 40, 5), AimedSub(5, 0, 5).move("forward", 8))

    @Test
    fun `aimed sub can move aim up`() = assertEquals(AimedSub(13, 40, 2), AimedSub(13, 40, 5).move("up", 3))

    @Test
    fun `aimed sub can follow orders`() = assertEquals(AimedSub(15, 60, 10), AimedSub(0, 0, 0).followOrders(commands))
}