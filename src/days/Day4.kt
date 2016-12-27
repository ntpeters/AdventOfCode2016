package days

import java.io.File
import java.util.*

class Day4 : Day {
    override fun invoke(input: File?) {
        println("The sector ID sum for real rooms is: ${part1(input)}")
    }

    fun part1(input: File?): Int =
        input?.readLines()?.sumBy {
            val data = it.split('-')
            val name = data.subList(0, data.lastIndex)
            val openBracketIndex = data.last().lastIndexOf('[')
            val id = data.last().substring(0, openBracketIndex)
            val checksum = data.last().substring(openBracketIndex + 1, data.last().lastIndex)
            if (validChecksum(name, checksum)) id.toInt() else 0
        } ?: 0

    fun validChecksum(name: List<String>, checksum: String): Boolean {
        val characterCounts = HashMap<Char, Int>()
        name.forEach {
            it.forEach {
                characterCounts.put(it, characterCounts.getOrDefault(it, 0) + 1)
            }
        }

        val nameChecksum = characterCounts.entries.sortedWith(Comparator { a, b ->
            val valCompare = b.value.compareTo(a.value)
            val keyCompare = a.key.toInt().compareTo(b.key.toInt())
            if (valCompare == 0) keyCompare else valCompare
        }).map { it.key }.take(5).joinToString("")

        return nameChecksum == checksum
    }
}