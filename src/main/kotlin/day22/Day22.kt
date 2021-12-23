package day22

import java.io.File
import java.lang.Integer.max
import java.lang.Integer.min

fun main() {
    val commands = File("cubes.txt").readLines().map { parseLine(it) }
    val initCubes = reboot(commands, initOnly = true)
    println("After an init-only reboot, there are $initCubes cubes turned on")
    val fullCubes = reboot(commands, initOnly = false)
    println("After a full reboot, there are $fullCubes cubes turned on")
}

typealias Command = Pair<String, Region>

private val pattern = Regex("""(on|off) x=(.*)\.\.(.*),y=(.*)\.\.(.*),z=(.*)\.\.(.*)""")
fun parseLine(line: String): Command {
    val (command, xMin, xMax, yMin, yMax, zMin, zMax) = pattern.matchEntire(line)!!.destructured
    return command to Region(xMin.toInt(), xMax.toInt(), yMin.toInt(), yMax.toInt(), zMin.toInt(), zMax.toInt())
}

data class Region(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int, val zMin: Int, val zMax: Int) {
    val litCount: ULong = (xMax - xMin + 1).toULong() * (yMax - yMin + 1).toULong() * (zMax - zMin + 1).toULong()

    fun isInInitializationRange(): Boolean = listOf(xMin, xMax, yMin, yMax, zMin, zMax).all { it >= -50 && it <= 50}

    fun intersects(other: Region): Boolean = !(xMin > other.xMax || xMax < other.xMin || yMin > other.yMax
            || yMax < other.yMin || zMin > other.zMax || zMax < other.zMin)

    fun intersection(other: Region): Region? = if (this.intersects(other))
        Region(
            max(xMin, other.xMin),
            min(xMax, other.xMax),
            max(yMin, other.yMin),
            min(yMax, other.yMax),
            max(zMin, other.zMin),
            min(zMax, other.zMax)
        ) else null

    fun remove(cutter: Region): List<Region> = buildList {
        // Top
        var zLower = cutter.zMax + 1
        if (zLower <= zMax) {
            add(Region(xMin, xMax, yMin, yMax, zLower, zMax))
            zLower -= 1
        } else
            zLower = zMax

        // Bottom
        var zUpper = cutter.zMin - 1
        if (zUpper >= zMin) {
            add(Region(xMin, xMax, yMin, yMax, zMin, zUpper))
            zUpper += 1
        } else
            zUpper = zMin

        // Right/Long
        var xLower = cutter.xMax + 1
        if (xLower <= xMax) {
            add(Region(xLower, xMax, yMin, yMax, zUpper, zLower)) // Note the flipped Z axis
            xLower -= 1
        } else
            xLower = xMax

        // Left/Long
        var xUpper = cutter.xMin - 1
        if (xUpper >= xMin) {
            add(Region(xMin, xUpper, yMin, yMax, zUpper, zLower)) // Note the flipped Z axis
            xUpper += 1
        } else
            xUpper = xMin

        // Back
        val yLower = cutter.yMax + 1
        if (yLower <= yMax) add(
            Region(
                xUpper,
                xLower,
                yLower,
                yMax,
                zUpper,
                zLower
            )
        ) // Flipped X and Z axes

        val yUpper = cutter.yMin - 1
        if (yUpper >= yMin) add(
            Region(
                xUpper,
                xLower,
                yMin,
                yUpper,
                zUpper,
                zLower
            )
        ) // Flipped X and Z axes
    }
}

fun reboot(commands: List<Command>, initOnly: Boolean): ULong {
    var turnedOn = setOf<Region>()

    for ((command, region) in commands) {
        if (initOnly && !region.isInInitializationRange()) continue

        turnedOn = turnedOn.flatMap { if (it.intersects(region)) it.remove(it.intersection(region)!!) else listOf(it) }
            .plus(if (command == "on") listOf(region) else listOf())
            .toSet()
    }

    return turnedOn.sumOf { it.litCount }
}


//
// I did a naive solution for part 1, knowing full well part 2 would likely be harder than that. I spent a bunch of
// time thinking through the solution, and got as far as "keep a list of disjoint 'on' regions" but couldn't wrap my
// head around how to split regions. So, I looked at the Reddit thread for hints, and built my solution around
// the Python implementation at:
//
// https://topaz.github.io/paste/#XQAAAQCFFQAAAAAAAAAzHIoib6p4r/McpYgEEgWhHoa5LSRMkVi92ASWXgRJn/53djPNeuaSjtWhoDNbuSFME3usC6n/b1rMDM/irH5cUQz7Wccrxho5OnqjPF7JiCge1MdCTrFuiWNyoou6CtrjGwhydQLqvENohnHclmEGSXZ4R+T049qSdrEcuJpOow5RP3zN7IsRzbVXBDyLDiDNQo2wBOn+t7hvQUh+j9+nNGNOyO4Lh2x2WX+ZJYiFfompRVYg/okXZfmvZoPof4NKvwDCUwjiuOUuOvubvkgb/BtLJ4gJVlvpxV/VEYyqy19py9qvl6Hy9sChdgcYNEpMIWlLblZ1K38xwpCQwhaYuNttEXXE949pJvS5nSI4EXa4clcxqs2jlgN5YStC49P8JR3YyYibNNd2ME6QbVId2zewFXGNE1+RQ14f/LzuGTyQH6gJhYZ5hgQ9adXK7a5lZ+X/IKSmN5YLyjdy/8xMX6ML3OYylO6lKc/K2Al2C4kWievpXMuzyU7s+u6yEPk3kNhFHjEupg/vM0ao0ALtpNHHGdnfYLQlI3BKeOWvnflmJaichD0hHuVqaM9ixG21UQTDqzI8gaYaK+6KAqrMCeE2iDXK954Wuh1V/8QoNYHpVhH+MxePnyOPFbuf/WvSb61BHk/6oTKVu1W69k/KGTrhJ6QyRaHtben8ehFIcA/H/NajhLaUjZleEtDeNLXy0a0IizT89S4KTfHwWh4tPC/n5U7gkycHje2GrtuC7heXzPLFVEAb+fhRkky3cQdqu7ur6Rwv3ueNTZzFB3+eKJR7M1lRbzzzZmMzA0rKLTZdQqcgSnQf5oMe02+qaRNY3lHw81GIetL6XiAdCFd14yq8T5Ber2tJTfqiiLUhmMF08fj/3JKXxH5o5WGZ1vdkQVivbIzkSy9mUaZF10PN9inC0ZdT8dA4MEMYQwHdNFK3JRdcnQNl/KzUtvpl9pKRxdq5deZvNE3f5aopETy7mVXNKTyoy/2UbWwvmEKuD0MivdEDPkcx39ErxKPC6Zh5Tl9V0FfSXwEpHkGpqzt7IM+MS1gCWkJfyqTFCqrcwAIB/l4RjmpqGbWLy56kc56M+YjCh2rSC6MAZcg8ayrADpmBhNmndOHsFCXvqJrW+2GvshrahBBFV5AHmEXj61vNesxOYY3I5jYkUhgVgg8nebY/bqB12irUdWZu1HhDsGM9hJgsTFyoyWoni2RsRrF7idP8bwQ3guzt3R3wFWQaWzYbYE4JUIDQI2MrpogIcxvu2UXxSMtuhDgo8fT++39/ogjg7l2joYndueYmCkOxAakkDSudDx41asNbu/43t7W9zRPdajjAerkitQ/Fuak3vvCYK4MaVN6NCu517aEUIHbgQoGNgpttFclGzePq2vEtD6kW/IaLl/Q+kvG8Ej0mXzy9dsaVFwKBeEVOIXOuIF8bsgQ3mdJ0P+ekyr+CWoqsUMJE/UV5tyZgIE9WEG5PojkDzl7d6uoqCLo72bFJ82vu76i9jfpPj4/J6xpWXDgBbSkeG0nz6bOV4EYidRMqTSNjknvpW6oa2bvYvon+2J8NMRKhVimlG05r+9vc3PBve1AaQn27WPGGtmr/QjmvLHtTKBw7Gf0NqbBFHj4pnB7n7KA8kKaJJu54lcZ8nX/SQbilnhzDxak3H21D0XykifuFWLFb/cMu6N4qMWYSYfw5AX052s7fT2/pLWhcMsPOD77JkqzYKBeKKhsXdcJfzl5xwE4Dk/7jkYVzT1/BC1Jo0it3QA7XWkA4cmJ9FYtOPa942c6IHVCxTmS6Txe0rSvGCPlgaUPHmylErnGY+wewogvI57kiFPlNsjwvbi4cJuUbdrouLAtG5NBNWKNIqp80YBcTk0IjEkr6z+jfKlgiuDaNkhPN2UKG8WzmR4XLp7D03x6rTxv1q0XCySe+cKQ/Wg3Pnhl/gvXoTJMH/pgQ5A==
//
// And even _that_ took me forever...
//
