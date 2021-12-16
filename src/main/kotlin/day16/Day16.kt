package day16

import java.io.File


fun main() {
    val hexData = File("packet.txt").readText()
    val packet = parsePacket(hexData)
    println("The sum of versions is ${packet.sumVersions()}")
    println("The evaluated value is ${packet.evaluate()}")
}

fun parseToBinary(hexString: String): String = hexString.uppercase().map {
    when (it) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> throw IllegalArgumentException("Bad hex digit: $it")
    }
}.joinToString("")

sealed interface Packet {
    val version: Int
    val typeId: Int
}

data class Literal(
    override val version: Int,
    val value: Long
) : Packet {
    override val typeId = 4
}

data class Operator(
    override val version: Int,
    override val typeId: Int,
    val subPackets: List<Packet>
) : Packet

fun parsePacket(hex: String): Packet = parseBinaryPacket(parseToBinary(hex)).first

fun parseBinaryPacket(bits: String): Pair<Packet, String> {
    val version = bits.take(3).toInt(2)

    return when (val packetType = bits.drop(3).take(3).toInt(2)) {
        4 -> parseLiteral(version, bits.drop(6))
        else -> parseOperator(version, packetType, bits.drop(6))
    }
}

fun parseLiteral(version: Int, bits: String): Pair<Literal, String> {
    val digitGroups = bits.chunked(5).takeWhileInclusive { it.first() == '1' }
    val value = digitGroups.joinToString("") { it.drop(1) }.toLong(2)

    return Pair(Literal(version, value), bits.drop(digitGroups.size * 5))
}

fun parseOperator(version: Int, packetType: Int, bits: String): Pair<Operator, String> =
    if (bits.first() == '0')
        parseOperatorWithBitLength(version, packetType, bits.drop(1))
    else
        parseOperatorWithSubPacketCount(version, packetType, bits.drop(1))

fun parseOperatorWithBitLength(version: Int, packetType: Int, bits: String): Pair<Operator, String> {
    val length = bits.take(15).toInt(2)
    val subPackets = mutableListOf<Packet>()

    var remainder = bits.drop(15).take(length)
    while (remainder.isNotEmpty()) {
        val (packet, r) = parseBinaryPacket(remainder)
        subPackets.add(packet)
        remainder = r
    }

    return Pair(Operator(version, packetType, subPackets.toList()), bits.drop(15 + length))
}

fun parseOperatorWithSubPacketCount(version: Int, packetType: Int, bits: String): Pair<Operator, String> {
    val count = bits.take(11).toInt(2)
    val subPackets = mutableListOf<Packet>()

    var remainder = bits.drop(11)
    repeat(count) {
        val (packet, r) = parseBinaryPacket(remainder)
        subPackets.add(packet)
        remainder = r
    }

    return Pair(Operator(version, packetType, subPackets.toList()), remainder)
}

fun Packet.sumVersions(): Int = when (this) {
    is Literal -> version
    is Operator -> version + subPackets.sumOf { it.sumVersions() }
}

fun Packet.evaluate(): Long = when (this) {
    is Literal -> value
    is Operator -> when (typeId) {
        0 -> subPackets.sumOf { it.evaluate() }
        1 -> subPackets.map { it.evaluate() }.reduce { prod, x -> prod * x }
        2 -> subPackets.minOf { it.evaluate() }
        3 -> subPackets.maxOf { it.evaluate() }
        5 -> if (subPackets[0].evaluate() > subPackets[1].evaluate()) 1 else 0
        6 -> if (subPackets[0].evaluate() < subPackets[1].evaluate()) 1 else 0
        7 -> if (subPackets[0].evaluate() == subPackets[1].evaluate()) 1 else 0
        else -> throw IllegalArgumentException("Unknown packet type: $typeId")
    }
}

//
// Taken from https://jivimberg.io/blog/2018/06/02/implementing-takewhileinclusive-in-kotlin/
//
inline fun <T> Iterable<T>.takeWhileInclusive(
    predicate: (T) -> Boolean
): List<T> {
    var shouldContinue = true
    return takeWhile {
        val result = shouldContinue
        shouldContinue = predicate(it)
        result
    }
}