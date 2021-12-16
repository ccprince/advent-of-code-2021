package day16

import org.junit.jupiter.api.TestFactory
import kotlin.test.Test
import kotlin.test.assertEquals

class Day16Tests {

    @Test
    fun `can parse hex numbers into binary strings`() = assertEquals("110100101111111000101000", parseToBinary("D2FE28"))

    @Test
    fun `can parse a literal-value packet`() = assertEquals(Literal(6, 2021), parsePacket("D2FE28"))

    @Test
    fun `can parse a bit-length-specified operator packet`() = assertEquals(
        Operator(1, 6, listOf(Literal(6, 10), Literal(2, 20))),
        parsePacket("38006F45291200")
    )

    @Test
    fun `can parse a sub-packet-count-specified operator packet`() = assertEquals(
        Operator(7, 3, listOf(Literal(2, 1), Literal(4, 2), Literal(1, 3))),
        parsePacket("EE00D40C823060")
    )

    @TestFactory
    fun `can sum version numbers over a parsed packet`() {
        listOf(
            "8A004A801A8002F478" to 16,
            "620080001611562C8802118E34" to 12,
            "C0015000016115A2E0802F182340" to 23,
            "A0016C880162017C3686B18A3D4780" to 31
        ).map { (packet, expected) ->
            assertEquals(expected, parsePacket(packet).sumVersions())
        }
    }

    @Test
    fun `can sum version numbers over a packet hierarchy`() = assertEquals(
        15,
        Operator(5, 3, listOf(Literal(2, 999), Literal(8, 999))).sumVersions()
    )

    @Test
    fun `can evaluate a sum operator`() = assertEquals(3, parsePacket("C200B40A82").evaluate())

    @Test
    fun `can evaluate a product operator`() = assertEquals(54, parsePacket("04005AC33890").evaluate())

    @Test
    fun `can evaluate a minimum operator`() = assertEquals(7, parsePacket("880086C3E88112").evaluate())

    @Test
    fun `can evaluate a maximum operator`() = assertEquals(9, parsePacket("CE00C43D881120").evaluate())

    @Test
    fun `can evaluate a less-than operator`() = assertEquals(1, parsePacket("D8005AC2A8F0").evaluate())

    @Test
    fun `can evaluate a greater-than operator`() = assertEquals(0, parsePacket("F600BC2D8F").evaluate())

    @Test
    fun `can evaluate an equals operator`() = assertEquals(0, parsePacket("9C005AC2F8F0").evaluate())

    @Test
    fun `can evaluate another equals operator`() = assertEquals(1, parsePacket("9C0141080250320F1802104A08").evaluate())
}
