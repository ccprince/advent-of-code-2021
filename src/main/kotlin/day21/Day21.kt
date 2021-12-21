package day21

import java.lang.Integer.min
import java.lang.Long.max

fun main() {
    val (player1, player2, rolls) = playDirac(6, 2)
    val result = min(player1, player2) * rolls
    println("The practice game scores are $player1 to $player2, resulting in $result")

    val (quantumPlayer1, quantumPlayer2) = playQuantumDirac(6, 2)
    println("The quantum game scores are $quantumPlayer1 to $quantumPlayer2")
    println(
        "Player ${if (quantumPlayer1 > quantumPlayer2) 1 else 2} won in ${
            max(
                quantumPlayer1,
                quantumPlayer2
            )
        } universes."
    )
}

data class DiracState(
    val player1Position: Int,
    val player2Position: Int,
    val player1Score: Int = 0,
    val player2Score: Int = 0,
    val currentPlayer: Int = 1,
    val maxScore: Int = 1000
) {
    val gameOver: Boolean = player1Score >= maxScore || player2Score >= maxScore

    fun playTurn(diceRoll: Int): DiracState =
        if (currentPlayer == 1) {
            val next = nextSpace(player1Position, diceRoll)
            DiracState(next, player2Position, player1Score + next, player2Score, 2, maxScore)
        } else {
            val next = nextSpace(player2Position, diceRoll)
            DiracState(player1Position, next, player1Score, player2Score + next, 1, maxScore)
        }

    /*
     * Each turn will spawn 27 universes, but there are only 7 different versions, depending on the sum of three dice.
     * We don't need to track each universe separately, because each copy will behave the same way. So, just keep a count
     * of how many copies of each universe there are. This way, instead of tracking hundreds of trillions of games, we
     * only need to keep track of the ~88,000 possible states for a game. (Ten spaces for each player, maximum score of
     * 21 for each player, 2 possible current players)
     */
    fun playQuantumTurn(universeCount: Long): List<Pair<DiracState, Long>> {
        val newDuplicates = mapOf(3 to 1, 4 to 3, 5 to 6, 6 to 7, 7 to 6, 8 to 3, 9 to 1)
        return newDuplicates.map { (roll, multiple) -> playTurn(roll) to universeCount * multiple }
    }

    private fun nextSpace(position: Int, roll: Int): Int = (position - 1 + roll) % 10 + 1
}

class DeterministicDie {
    private var current = 1
    private fun roll(): Int {
        val result = current
        current = if (current == 100) 1 else current + 1
        return result
    }

    fun rollThree(): Int = (1..3).sumOf { roll() }
}

fun playDirac(player1Start: Int, player2Start: Int): Triple<Int, Int, Int> {
    var state = DiracState(player1Start, player2Start)
    val die = DeterministicDie()
    var rolls = 0
    while (!state.gameOver) {
        state = state.playTurn(die.rollThree())
        rolls += 3
    }

    return Triple(state.player1Score, state.player2Score, rolls)
}

fun playQuantumDirac(player1Start: Int, player2Start: Int): Pair<Long, Long> {
    var player1WinCount = 0L
    var player2WinCount = 0L
    var universes = listOf(DiracState(player1Start, player2Start, maxScore = 21) to 1L)

    while (universes.isNotEmpty()) {
        val newUniverses = universes.flatMap { (state, count) -> state.playQuantumTurn(count) }
            .groupBy({ it.first }, { it.second })
            .map { (key, value) -> key to value.sum() }
        val (complete, incomplete) = newUniverses.partition { (state, _) -> state.gameOver }
        val (player1Wins, player2Wins) = complete.partition { (state, _) -> state.player1Score > state.player2Score }
        player1WinCount += player1Wins.sumOf { (_, count) -> count }
        player2WinCount += player2Wins.sumOf { (_, count) -> count }
        universes = incomplete
    }

    return Pair(player1WinCount, player2WinCount)
}