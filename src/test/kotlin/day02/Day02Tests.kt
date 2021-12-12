package day02

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day02Tests {

    private val commands = listOf(
        "forward 5",
        "down 5",
        "forward 8",
        "up 3",
        "down 8",
        "forward 2"
    )


    @Test
    fun `position can move forward`() = assertEquals(Position(5, 2), Position(2, 2).move("forward", 3))

    @Test
    fun `position can move up`() = assertEquals(Position(5, 2), Position(5, 4).move("up", 2))

    @Test
    fun `position can move down`() = assertEquals(Position(5, 2), Position(5, 0).move("down", 2))

    @Test
    fun `sub can follow orders`() = assertEquals(Position(15, 10), Position(0, 0).followOrders(commands))

    @Test
    fun `aimable position can move aim down`() =
        assertEquals(PositionAndAim(5, 0, 5), PositionAndAim(5, 0, 0).move("down", 5))

    @Test
    fun `aimable position can move forward`() =
        assertEquals(PositionAndAim(13, 40, 5), PositionAndAim(5, 0, 5).move("forward", 8))

    @Test
    fun `aimable position can move aim up`() =
        assertEquals(PositionAndAim(13, 40, 2), PositionAndAim(13, 40, 5).move("up", 3))

    @Test
    fun `aimable position can follow orders`() =
        assertEquals(PositionAndAim(15, 60, 10), PositionAndAim(0, 0, 0).followOrders(commands))
}

