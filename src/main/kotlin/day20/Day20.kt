package day20

import java.io.File

fun main() {
    val (algorithm, image) = parseInput(File("image.txt").readLines())
    val transformed = image.transform(algorithm, 2)
    println("After transforming twice, there are ${transformed.countLightPixels()} light pixels")

    val transformed50 = image.transform(algorithm, 50)
    println("After transforming 50 times, there are ${transformed50.countLightPixels()} light pixels")
}

typealias Point = Pair<Int, Int>
typealias Algorithm = CharArray

data class Image(val pixels: Map<Point, Char>, val background: Char = '.') {
    private val xRange = pixels.keys.let { (it.minOf { (x, _) -> x }..it.maxOf { (x, _) -> x }) }
    private val yRange = pixels.keys.let { (it.minOf { (_, y) -> y }..it.maxOf { (_, y) -> y }) }

    private fun pixelAt(x: Int, y: Int): Char =
        if (x in xRange && y in yRange) pixels[Point(x, y)] ?: '.' else background

    fun valueAt(x: Int, y: Int): Int = ((y - 1)..(y + 1)).flatMap { yy ->
        ((x - 1)..(x + 1)).map { xx -> if (pixelAt(xx, yy) == '#') '1' else '0' }
    }.joinToString("").toInt(2)

    fun transform(algorithm: Algorithm, times: Int): Image {
        var image = this
        repeat(times) {
            val newPixels = image.xRange.expanded().flatMap { x ->
                image.yRange.expanded().map { y -> Point(x, y) to algorithm[image.valueAt(x, y)] }
            }.filter { (_, c) -> c == '#' }
                .toMap()

            val newBackground = if (image.background == '.') algorithm[0] else algorithm[511]

            image = Image(newPixels, newBackground)
        }

        return image
    }

    // Note that this assumes the infinite pixels are all dark
    fun countLightPixels(): Int = pixels.values.count { it == '#' }

    fun asString(): String = yRange.expanded().joinToString("\n") { y ->
        xRange.expanded().map { x -> pixelAt(x, y) }.joinToString("")
    }
}

fun parseInput(data: List<String>): Pair<Algorithm, Image> {
    val algorithm = data[0].toCharArray()
    val pixels = data.drop(2).flatMapIndexed { y, row ->
        row.mapIndexed { x, pixel ->
            Pair(x, y) to pixel
        }
    }.toMap()
    return Pair(algorithm, Image(pixels))
}

fun IntRange.expanded(): IntRange = IntRange(first - 1, last + 1)