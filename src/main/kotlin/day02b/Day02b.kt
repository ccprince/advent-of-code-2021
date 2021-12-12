package day02b

import java.io.File
import kotlin.math.exp

// This is the same pair of problems as Day02 -- I just wanted to try it with proper inheritance.
// Wow, I have to generate a _lot_ of code to make this happen. Data classes really do a lot.

fun main() {
    val commands = File("commands.txt").readLines()
    val sub = Sub().followOrders(commands)
    printTest(sub, 1524750)

    val aimedSub = AimedSub().followOrders(commands)
    printTest(aimedSub, 1592426537)
}

fun printTest(sub: Sub, expectedProduct: Int): Unit {
    println("The sub moved to $sub")
    val product = sub.horizontal * sub.depth
    println("The product is $product (${if (expectedProduct == product) "matches" else "doesn't match"})")
}

open class Sub(val horizontal: Int = 0, val depth: Int = 0) {
    fun followOrders(commands: List<String>): Sub = commands.fold(this) { sub, cmd ->
        val params = cmd.split(" ")
        sub.move(params[0], params[1].toInt())
    }

    open fun move(command: String, value: Int): Sub = when(command) {
        "forward" -> Sub(horizontal + value, depth)
        "up" -> Sub(horizontal, depth - value)
        "down" -> Sub(horizontal, depth + value)
        else -> throw IllegalArgumentException("Unknown command: $command")
    }

    override fun toString(): String {
        return "Sub(horizontal=$horizontal, depth=$depth)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sub

        if (horizontal != other.horizontal) return false
        if (depth != other.depth) return false

        return true
    }

    override fun hashCode(): Int {
        var result = horizontal
        result = 31 * result + depth
        return result
    }
}

class AimedSub(horizontal: Int = 0, depth: Int = 0, val aim: Int = 0) : Sub(horizontal, depth) {
    override fun move(command: String, value: Int): AimedSub = when(command) {
        "forward" -> AimedSub(horizontal + value, depth + aim * value, aim)
        "down" -> AimedSub(horizontal, depth, aim + value)
        "up" -> AimedSub(horizontal, depth, aim - value)
        else -> throw IllegalArgumentException("Unknown command $command")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as AimedSub

        if (aim != other.aim) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + aim
        return result
    }

    override fun toString(): String {
        return "AimedSub(horizontal=$horizontal, depth=$depth, aim=$aim)"
    }
}