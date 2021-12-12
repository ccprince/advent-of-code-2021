package day02

import java.io.File

fun main() {
    val commands = File("commands.txt").readLines()
    val position = Position(0, 0).followOrders(commands)
    println("The sub moved to $position")
    println("The product of the position is ${position.horizontal * position.depth}")

    println()
    val aimablePosition = PositionAndAim(0, 0, 0).followOrders(commands)
    println("The aimable sub moved to $aimablePosition")
    println("The product of the position is ${aimablePosition.horizontal * aimablePosition.depth}")
}

interface Sub {
    val horizontal: Int
    val depth: Int

    fun move(command: String, value: Int): Sub

    fun followOrders(commands: List<String>): Sub = commands.fold(this) { sub, cmd ->
        val params = cmd.split(" ")
        sub.move(params[0], params[1].toInt())
    }
}

data class Position(override val horizontal: Int = 0, override val depth: Int = 0): Sub {
    override fun move(command: String, value: Int): Sub = when (command) {
        "forward" -> copy(horizontal = horizontal + value)
        "up" -> copy(depth = depth - value)
        "down" -> copy(depth = depth + value)
        else -> throw IllegalArgumentException("Unknown command $command")
    }
}

//fun Sub.followOrders(commands: List<String>): Sub = commands.fold(this) { sub, cmd ->
//    val params = cmd.split(" ")
//    sub.move(params[0], params[1].toInt())
//}

data class PositionAndAim(override val horizontal: Int, override val depth: Int, val aim: Int) : Sub {
    override fun move(command: String, value: Int): Sub = when (command) {
        "up" -> copy(aim = aim - value)
        "down" -> copy(aim = aim + value)
        "forward" -> copy(horizontal = horizontal + value, depth = depth + aim * value)
        else -> throw IllegalArgumentException("Unknown command $command")
    }
}
