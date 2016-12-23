package days

import java.io.File
import java.util.regex.Pattern

class Day3 : Day {
    override fun invoke(input: File?) {
        println("There are ${part1(input)} valid triangles")
    }

    fun readTrianglesFromFile(input: File?): List<Triple<Int, Int, Int>> =
            input?.readLines()
                    ?.map { it.trim()
                            .split(Pattern.compile("\\s+"))
                            .map(String::toInt)
                            .toOrderedTripleOrNull() }
                    ?.filterNotNull()
                    ?: emptyList<Triple<Int, Int, Int>>()

    fun part1(input: File?): Int = readTrianglesFromFile(input).count { validTriangle(it) }

    fun validTriangle(triangle: Triple<Int, Int, Int>): Boolean = triangle.first + triangle.second > triangle.third
}

fun <T : Comparable<T>> List<T>.toOrderedTripleOrNull(): Triple<T, T, T>? =
    if (this.size == 3) {
        val sorted = this.sorted()
        Triple(sorted[0], sorted[1], sorted[2])
    } else {
        null
    }