package days

import java.io.File
import java.util.*
import java.util.regex.Pattern

class Day3 : Day {
    override fun invoke(input: File?) {
        println("There are ${part1(input)} valid triangles")
        println("There are actually ${part2(input)} valid triangles")
    }

    fun readTrianglesFromFileByRow(input: File?): List<Triple<Int, Int, Int>> =
            input?.readLines()
                    ?.map { it.trim()
                            .split(Pattern.compile("\\s+"))
                            .map(String::toInt)
                            .toOrderedTripleOrNull() }
                    ?.filterNotNull()
                    ?: emptyList<Triple<Int, Int, Int>>()

    fun readTrianglesFromFileByColumn(input: File?): List<Triple<Int, Int, Int>> {
        val triangles = ArrayList<Triple<Int, Int, Int>>()

        var triangleBuffer1 = ArrayList<Int>()
        var triangleBuffer2 = ArrayList<Int>()
        var triangleBuffer3 = ArrayList<Int>()

        input?.readLines()?.forEach {
            val numbers = it.trim().split(Pattern.compile("\\s+")).map(String::toInt)
            triangleBuffer1.add(numbers[0])
            triangleBuffer2.add(numbers[1])
            triangleBuffer3.add(numbers[2])

            val triangle1 = triangleBuffer1.toOrderedTripleOrNull()
            val triangle2 = triangleBuffer2.toOrderedTripleOrNull()
            val triangle3 = triangleBuffer3.toOrderedTripleOrNull()

            if (triangle1 != null) {
                triangles.add(triangle1)
                triangleBuffer1 = ArrayList()
            }
            if (triangle2 != null) {
                triangles.add(triangle2)
                triangleBuffer2 = ArrayList()
            }
            if (triangle3 != null) {
                triangles.add(triangle3)
                triangleBuffer3 = ArrayList()
            }
        }

        return triangles
    }

    fun part1(input: File?): Int = readTrianglesFromFileByRow(input).count { validTriangle(it) }
    fun part2(input: File?): Int = readTrianglesFromFileByColumn(input).count { validTriangle(it) }

    fun validTriangle(triangle: Triple<Int, Int, Int>): Boolean = triangle.first + triangle.second > triangle.third
}

fun <T : Comparable<T>> List<T>.toOrderedTripleOrNull(): Triple<T, T, T>? =
    if (this.size == 3) {
        val sorted = this.sorted()
        Triple(sorted[0], sorted[1], sorted[2])
    } else {
        null
    }