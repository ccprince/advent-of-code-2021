package day23

import combinations
import day23.Amphipod.*
import day23.SpotType.*
import java.io.File
import java.util.*

fun main() {
    val lines = File("amphipods.txt").readLines()
    val start = parseState(lines)
    val bestCost = bestCostToOrganize(start)
    println("The best cost to organize the amphipods is $bestCost")

    val largeLines = lines.take(3) + listOf("  #D#C#B#A#", "  #D#B#A#C#") + lines.drop(3)
    val largeStart = parseState(largeLines)
    val bestCostLarge = bestCostToOrganize(largeStart, roomSize = 4)
    println("The best cost to organize the amphipods in the large cave is $bestCostLarge")
}

enum class Amphipod {
    AMBER, BRONZE, COPPER, DESERT;

    fun costPerStep(): Int = when (this) {
        AMBER -> 1
        BRONZE -> 10
        COPPER -> 100
        DESERT -> 1000
    }
}

enum class SpotType { HALLWAY, ROOM }
typealias Location = Pair<Int, Int>

data class Spot(val type: SpotType, val location: Location, val stepsToNeighbor: Map<Location, Int>)

class CaveMap(val roomSize: Int = 2) {
    val spotAt = listOf(
        Spot(HALLWAY, Location(0, 0), mapOf(Location(1, 0) to 1)),
        Spot(HALLWAY, Location(1, 0), mapOf(Location(0, 0) to 1, Location(2, 1) to 2, Location(3, 0) to 2)),
        Spot(
            HALLWAY,
            Location(3, 0),
            mapOf(Location(1, 0) to 2, Location(2, 1) to 2, Location(4, 1) to 2, Location(5, 0) to 2)
        ),
        Spot(
            HALLWAY,
            Location(5, 0),
            mapOf(Location(3, 0) to 2, Location(4, 1) to 2, Location(6, 1) to 2, Location(7, 0) to 2)
        ),
        Spot(
            HALLWAY,
            Location(7, 0),
            mapOf(Location(5, 0) to 2, Location(6, 1) to 2, Location(8, 1) to 2, Location(9, 0) to 2)
        ),
        Spot(HALLWAY, Location(9, 0), mapOf(Location(7, 0) to 2, Location(8, 1) to 2, Location(10, 0) to 1)),
        Spot(HALLWAY, Location(10, 0), mapOf(Location(9, 0) to 1)),
        Spot(ROOM, Location(2, 1), mapOf(Location(2, 2) to 1, Location(1, 0) to 2, Location(3, 0) to 2)),
        Spot(ROOM, Location(4, 1), mapOf(Location(4, 2) to 1, Location(3, 0) to 2, Location(5, 0) to 2)),
        Spot(ROOM, Location(6, 1), mapOf(Location(6, 2) to 1, Location(5, 0) to 2, Location(7, 0) to 2)),
        Spot(ROOM, Location(8, 1), mapOf(Location(8, 2) to 1, Location(7, 0) to 2, Location(9, 0) to 2)),
    ).plus((2..roomSize).flatMap { roomRow(it) })
        .associateBy { it.location }

    private fun roomRow(i: Int): List<Spot> = if (i == roomSize)
        (2..8 step 2).map { n -> Spot(ROOM, Location(n, i), mapOf(Location(n, i - 1) to 1)) }
    else
        (2..8 step 2).map { n -> Spot(ROOM, Location(n, i), mapOf(Location(n, i - 1) to 1, Location(n, i + 1) to 1)) }

    val destinations = mapOf(
        AMBER to (1..roomSize).map { Location(2, it) },
        BRONZE to (1..roomSize).map { Location(4, it) },
        COPPER to (1..roomSize).map { Location(6, it) },
        DESERT to (1..roomSize).map { Location(8, it) }
    )

    val stepCount = spotAt.keys.combinations(2).flatMap { (a, b) ->
        val steps = countSteps(a, b)
        listOf(Move(a, b) to steps, Move(b, a) to steps)
    }.toMap()

    fun countSteps(from: Location, to: Location): Int {
        val visited = mutableSetOf<Location>()
        val q = PriorityQueue<Pair<Location, Int>>(compareBy { it.second }).apply { add(Pair(from, 0)) }

        while (q.isNotEmpty()) {
            val p = q.poll()

            if (p.first == to) {
                return p.second
            }

            if (p.first !in visited) {
                visited.add(p.first)
                spotAt[p.first]!!.stepsToNeighbor
                    .filter { (l, _) -> l !in visited }
                    .forEach { (l, s) ->
                        q.offer(Pair(l, p.second + s))
                    }
            }
        }

        throw RuntimeException("never found the end point")
    }

}


typealias Move = Pair<Location, Location>
typealias State = Map<Location, Amphipod>

fun State.isOrganized(caveMap: CaveMap): Boolean =
    entries.all { (location, amphipod) -> location in caveMap.destinations.getValue(amphipod) }

fun State.validMoves(caveMap: CaveMap): Set<Move> = this.keys.flatMap { validMovesFrom(it, caveMap) }.toSet()

fun State.validMovesFrom(from: Location, caveMap: CaveMap): Set<Move> {
    val mover = this[from] ?: throw IllegalArgumentException("Nothing to move at $from")
    val fromSpot = caveMap.spotAt[from] ?: throw RuntimeException("Invalid location $from")
    val dest = caveMap.destinations[mover] ?: throw RuntimeException("No destinations for $mover")

    // Don't move anything that is in the right room and not blocking a wrong one from getting out
    if (from in dest && !dest.any { it.second > from.second && this[it] != mover })
        return setOf()

    val visited = mutableSetOf(from)
    val toVisit: Queue<Location> =
        LinkedList<Location>().apply { fromSpot.stepsToNeighbor.forEach { (l, _) -> offer(l) } }
    val valid = mutableSetOf<Location>()

    while (toVisit.isNotEmpty()) {
        val p = toVisit.poll()
        val pSpot = caveMap.spotAt[p] ?: throw RuntimeException("Invalid location $p")

        if (p in visited)
            continue

        visited.add(p)

        if (this.containsKey(p)) {
            // This spot is occupied
            continue
        }

        val hallwayToHallway = fromSpot.type == HALLWAY && pSpot.type == HALLWAY
        val intoWrongDestination = pSpot.type == ROOM && p !in dest
        val intoRightDestinationWithOtherTypeInIt =
            pSpot.type == ROOM && p in dest && dest.any { this[it] != null && this[it] != mover }
        val canMoveFurtherIntoRoom = pSpot.type == ROOM && p in dest && dest.any { it.second > p.second && this[it] == null }

        if (!hallwayToHallway && !intoWrongDestination && !intoRightDestinationWithOtherTypeInIt && !canMoveFurtherIntoRoom)
            valid.add(p)

        pSpot.stepsToNeighbor.filterNot { (l, _) -> l in visited }.forEach { (l, _) -> toVisit.offer(l) }
    }

    return valid.map { Move(from, it) }.toSet()
}

fun bestCostToOrganize(initial: State, roomSize: Int = 2): Int {
    val visited = mutableSetOf<State>();
    val q = PriorityQueue<Pair<State, Int>>(compareBy { it.second })
        .apply { offer(Pair(initial, 0)) }
    val caveMap = CaveMap(roomSize)

    while (q.isNotEmpty()) {
        val p = q.poll()

        if (p.first.isOrganized(caveMap)) {
            return p.second
        }

        if (p.first !in visited) {
            visited.add(p.first)
            p.first.validMoves(caveMap).forEach { m ->
                val newState = p.first.applyMove(m)
                val newCost = p.second + caveMap.stepCount[m]!! * p.first[m.first]!!.costPerStep()
                if (newState !in visited)
                    q.offer(Pair(newState, newCost))
            }
        }
    }

    throw RuntimeException("Never organized the amphipods")
}

fun State.applyMove(move: Move): State = map { (l, a) -> if (l == move.first) move.second to a else l to a }.toMap()

fun parseState(input: List<String>): State = input.flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, c ->
        when (c) {
            'A' -> Pair(x - 1, y - 1) to AMBER
            'B' -> Pair(x - 1, y - 1) to BRONZE
            'C' -> Pair(x - 1, y - 1) to COPPER
            'D' -> Pair(x - 1, y - 1) to DESERT
            else -> null
        }
    }
}.toMap()