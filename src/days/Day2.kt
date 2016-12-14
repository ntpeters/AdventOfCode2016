package days

import java.io.File
import java.util.*

class Day2 : Day {
    override fun invoke(input: File?) {
        println("The bathroom code is: ${part1(input)}")
    }

    fun part1(input: File?): String {
        val directions = readDirectionsFromFile(input)
        return getDoorCode(directions)
    }

    fun readDirectionsFromFile(input: File?): List<List<Direction>> = input?.readLines()?.map { it.map { Direction.fromChar(it) } } ?: emptyList()

    fun getDoorCode(directions: List<List<Direction>>): String {
        val keys = ArrayList<Int>()
        directions.forEach { keys.add(getNextDigit(keys.lastOrNull() ?: 4, it)) }
        return keys.map { it + 1 }.joinToString("")
    }

    fun getNextDigit(currentDigit: Int, directions: List<Direction>): Int =
            directions.fold(currentDigit) { digit, it ->
                when (it) {
                    Direction.UP -> if (digit > 2) digit - 3 else digit
                    Direction.DOWN -> if (digit < 6) digit + 3 else digit
                    Direction.RIGHT -> if ((digit + 1) % 3 > digit % 3) digit + 1 else digit
                    Direction.LEFT -> if ((digit - 1) % 3 < digit % 3 && digit > 0) digit - 1 else digit
                }
            }

    enum class Direction {
        UP, DOWN, LEFT, RIGHT;
        companion object {

            fun fromChar(character: Char): Direction =
                    when (character) {
                        'U' -> UP
                        'D' -> DOWN
                        'R' -> RIGHT
                        'L' -> LEFT
                        else -> throw IllegalArgumentException("$this does not denote a valid Direction")
                    }
        }
    }
}