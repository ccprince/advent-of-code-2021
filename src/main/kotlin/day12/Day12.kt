package day12

import java.io.File

fun main() {
    val caveMap = buildCaveMap(File("caves.txt").readLines())
    println("There are ${countPaths(caveMap)} paths through the caves")
    println("There are ${countLongerPaths(caveMap)} paths through the caves")
}

data class Cave(val name: String) {
    val isSmall = name.all { it.isLowerCase() }
}


typealias CaveMap = Map<Cave, Set<Cave>>
typealias Path = List<Cave>

fun buildCaveMap(data: List<String>): CaveMap =
    data.flatMap { line ->
        val names = line.split("-")
        setOf(Cave(names[0]) to Cave(names[1]), Cave(names[1]) to Cave(names[0]))
    }.groupBy({ it.first }, { it.second })
        .mapValues { it.value.toSet() }


fun findPaths(caves: CaveMap, candidateFilter: (Cave, Path) -> Boolean): Set<Path> {
    val alreadyChecked = mutableSetOf<Path>()
    tailrec fun recurse(candidates: List<Path>, paths: Set<Path>): Set<Path> =
        if (candidates.isEmpty())
            paths
        else {
            val p = candidates.first()
            if (p.last() == Cave("end"))
                recurse(candidates.drop(1), paths + setOf(p))
            else {
                val nextCaves = caves[p.last()]!!
                    .filter { candidateFilter(it, p) }
                val nextPaths = nextCaves.map { p + it }
                    .filter { !alreadyChecked.contains(it) }
                recurse(candidates.drop(1) + nextPaths, paths)
            }
        }

    return recurse(listOf(listOf(Cave("start"))), setOf())
}

fun countPaths(caves: CaveMap): Int =
    findPaths(caves) { nextCave, path -> !(nextCave.isSmall && path.contains(nextCave)) }.size

fun countLongerPaths(caves: CaveMap): Int = findPaths(caves) { nextCave, path ->
    when {
        !nextCave.isSmall -> true
        nextCave == Cave("start") -> false
        nextCave == Cave("end") -> true
        path.count { it == nextCave } > 1 -> false
        else -> {
            val d = doubleSmallCave(path)
            if (d == null)
                true
            else
                d != nextCave && path.count { it == nextCave } == 0
        }
    }

}.size

fun doubleSmallCave(p: Path): Cave? = p.filter { it.isSmall }.groupBy { it }.values.firstOrNull { it.size > 1 }?.get(0)
