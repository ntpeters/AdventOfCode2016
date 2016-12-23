package days

import java.io.File
import java.util.*

class Day2 : Day {
    override fun invoke(input: File?) {
        println("The bathroom code is: ${part1(input)}")
        println("The actual bathroom code is: ${part2(input)}")
    }

    fun part1(input: File?): String {
        val directions = readDirectionsFromFile(input)
        return getDoorCode(directions)
    }

    fun part2(input: File?): String {
        val directions = readDirectionsFromFile(input)
        val keypad = readKeypadFromFile(File("input/Day2-2.txt"))
        val start = Pair(2, 0)
        return getDoorCode(directions, keypad, start)
    }

    fun readDirectionsFromFile(input: File?): List<List<Direction>> = input?.readLines()?.map { it.map { Direction.fromChar(it) } } ?: emptyList()

    fun readKeypadFromFile(input: File?): HashMap<Pair<Int, Int>, Char> =
        hashMapOf(*(input
                ?.readLines()
                ?.mapIndexed { i, line -> line
                        .mapIndexed { j, value -> if (value != ' ') Pair(Pair(i, j), value) else null }
                        .filterNotNull() }
                ?.flatMap { it }
                ?.toTypedArray()
                ?: emptyArray<Pair<Pair<Int, Int>, Char>>()))

    fun getDoorCode(directions: List<List<Direction>>, keypad: HashMap<Pair<Int, Int>, Char>, start: Pair<Int, Int>): String {
        var currentLocation = Pair(start, keypad[start])
        return directions.map {
            currentLocation = getNextCharacter(currentLocation.first, it, keypad)
            currentLocation.second
        }.joinToString("")
    }

    fun getNextCharacter(startLocation: Pair<Int, Int>, directions: List<Day2.Direction>, keypad: HashMap<Pair<Int, Int>, Char>): Pair<Pair<Int, Int>, Char> {
        val nextLocation = directions.fold(startLocation) { location, it ->
            val newLocation =
                    when (it) {
                        Direction.UP -> Pair(location.first - 1, location.second)
                        Direction.DOWN -> Pair(location.first + 1, location.second)
                        Direction.LEFT -> Pair(location.first, location.second - 1)
                        Direction.RIGHT -> Pair(location.first, location.second + 1)
                    }
            if (keypad.containsKey(newLocation)) newLocation else location
        }
        return Pair(nextLocation, keypad[nextLocation] as Char)
    }

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
        UP, DOWN, RIGHT, LEFT;
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