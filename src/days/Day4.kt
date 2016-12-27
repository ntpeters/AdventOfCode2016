package days

import java.io.File
import java.util.*

class Day4 : Day {
    override fun invoke(input: File?) {
        println("The sector ID sum for real rooms is: ${part1(input)}")
        println("The sector ID where the North Pole objects are stored is ${part2(input)}")
    }

    fun part1(input: File?): Int =
        input?.readLines()?.sumBy {
            val room = makeRoom(it)
            if (room.hasValidChecksum()) room.sectorId else 0
        } ?: 0

    fun part2(input: File?): String =
        input?.readLines()
                ?.map { makeRoom(it) }
                ?.filter { it.hasValidChecksum() }
                ?.filter { it.decryptName() == "northpole object storage" }
                ?.firstOrNull()?.sectorId?.toString()
                ?: "not found"

    fun makeRoom(input: String): Room {
        val data = input.split('-')
        val name = data.subList(0, data.lastIndex)
        val openBracketIndex = data.last().lastIndexOf('[')
        val id = data.last().substring(0, openBracketIndex)
        val checksum = data.last().substring(openBracketIndex + 1, data.last().lastIndex)
        return Room(name, checksum, id.toInt())
    }

    class Room(val encryptedName: List<String>, val checksum: String, val sectorId: Int) {

        fun hasValidChecksum(): Boolean {
            val characterCounts = HashMap<Char, Int>()
            encryptedName.forEach {
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

        fun decryptName(): String = encryptedName.map { it.map { (((it - 97 + sectorId).toInt() % 26) + 97).toChar() }.joinToString("") }.joinToString(" ")
    }
}