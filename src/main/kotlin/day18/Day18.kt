package day18

import java.io.File
import java.lang.Character.isDigit
import kotlin.math.ceil

fun main() {
    val numbers = File("numbers.txt").readLines().map { snailNumberFromString(it) }
    val sum = numbers.sum()
    println("The magnitude is ${sum.magnitude()}")
    println("The largest additive magnitude is ${largestAdditiveMagnitude(numbers)}")
}

sealed interface SnailNumber

operator fun SnailNumber.plus(other: SnailNumber): SnailNumber = SnailPair(this, other).reduced()

fun SnailNumber.asString(): String = when (this) {
    is SnailPair -> "[" + left.asString() + "," + right.asString() + "]"
    is SnailRegular -> n.toString()
}

fun SnailNumber.magnitude(): Int = when (this) {
    is SnailRegular -> n
    is SnailPair -> left.magnitude() * 3 + right.magnitude() * 2
}

data class SnailPair(val left: SnailNumber, val right: SnailNumber) : SnailNumber {
    fun exploded(): SnailNumber {
        val explosion = findExplosionOrNull() ?: return this
        val replacement = SnailRegular(0)
        fun replaceExplosion(sn: SnailNumber): SnailNumber = when (sn) {
            is SnailRegular -> sn
            is SnailPair -> if (sn === explosion)
                replacement
            else
                SnailPair(replaceExplosion(sn.left), replaceExplosion(sn.right))
        }

        val replaced = replaceExplosion(this)
        val numbers = replaced.numbersInOrder()
        val i = numbers.indexOfFirst { it === replacement }
        val leftTarget = if (i > 0) numbers[i - 1] else null
        val rightTarget = if (i < numbers.size - 1) numbers[i + 1] else null

        fun spreadExplosion(sn: SnailNumber): SnailNumber = when (sn) {
            is SnailPair -> SnailPair(spreadExplosion(sn.left), spreadExplosion(sn.right))
            is SnailRegular -> when {
                sn === leftTarget -> SnailRegular(sn.n + (explosion.left as SnailRegular).n)
                sn === rightTarget -> SnailRegular(sn.n + (explosion.right as SnailRegular).n)
                else -> sn
            }
        }
        return spreadExplosion(replaced)
    }

    fun findExplosionOrNull(): SnailPair? {
        fun recurse(sn: SnailNumber, depth: Int): SnailPair? = when (sn) {
            is SnailRegular -> null
            is SnailPair -> if (depth == 4) sn else recurse(sn.left, depth + 1) ?: recurse(sn.right, depth + 1)
        }

        return recurse(this, 0)
    }
}

data class SnailRegular(val n: Int) : SnailNumber {
    fun split(): SnailPair = SnailPair(SnailRegular(n / 2), SnailRegular(ceil(n / 2.0).toInt()))
}

fun snailNumberFromString(s: String): SnailNumber = if (s.isNumber()) {
    SnailRegular(s.toInt())
} else {
    val (left, right) = parseHalvesOfPair(s)
    SnailPair(snailNumberFromString(left), snailNumberFromString(right))
}

fun String.isNumber(): Boolean = all { isDigit(it) }

fun parseHalvesOfPair(s: String): Pair<String, String> {
    var depth = 0
    for ((i, c) in s.substring(1, s.length - 1).withIndex()) {
        when (c) {
            '[' -> depth++
            ']' -> depth--
            ',' -> if (depth == 0) return Pair(s.substring(1, i + 1), s.substring(i + 2, s.length - 1))
        }
    }

    throw IllegalArgumentException("Malformed pair: $s")
}

fun List<SnailNumber>.sum(): SnailNumber = reduce { acc, n -> acc + n }

fun SnailNumber.numbersInOrder(): List<SnailRegular> = when (this) {
    is SnailRegular -> listOf(this)
    is SnailPair -> this.left.numbersInOrder() + this.right.numbersInOrder()
}

fun SnailNumber.splitTarget(): SnailRegular? = this.numbersInOrder().firstOrNull { it.n > 9 }

fun SnailNumber.split(): SnailNumber {
    val splitTarget = this.splitTarget()
    fun recurse(sn: SnailNumber): SnailNumber = when (sn) {
        is SnailPair -> SnailPair(recurse(sn.left), recurse(sn.right))
        is SnailRegular -> if (sn === splitTarget)
            SnailPair(SnailRegular(sn.n / 2), SnailRegular(ceil(sn.n / 2.0).toInt()))
        else sn
    }
    return recurse(this)
}

fun SnailNumber.reduced(): SnailNumber = when (this) {
    is SnailRegular -> this
    is SnailPair -> {
        var sn = this
        var done = false

        while (!done) {
            if (sn.needsExplosion())
                sn = (sn as SnailPair).exploded()
            else if (sn.needsSplit())
                sn = sn.split()
            else
                done = true
        }

        sn
    }
}

fun SnailNumber.needsExplosion(): Boolean = when (this) {
    is SnailRegular -> false
    is SnailPair -> findExplosionOrNull() != null
}

fun SnailNumber.needsSplit(): Boolean = when (this) {
    is SnailRegular -> false
    is SnailPair -> numbersInOrder().any { it.n > 9 }
}

fun largestAdditiveMagnitude(ns: List<SnailNumber>): Int =
    cartesianProduct(ns, ns).filterNot { (a, b) -> a === b }
        .maxOf { (a, b) -> (a + b).magnitude() }


/* Taken from https://gist.github.com/kiwiandroiddev/fef957a69f91fa64a46790977d98862b */

/**
 * E.g.
 * cartesianProduct(listOf(1, 2, 3), listOf(true, false)) returns
 *  [(1, true), (1, false), (2, true), (2, false), (3, true), (3, false)]
 */
fun <T, U> cartesianProduct(c1: Collection<T>, c2: Collection<U>): List<Pair<T, U>> {
    return c1.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
}