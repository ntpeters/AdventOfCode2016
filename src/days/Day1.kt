package days

import java.io.File

class Day1 : Day {
    override operator fun invoke(input: File?) {
        print(part1(input))
    }

    fun part1(input: File?): Int {
        val instructions = input?.readText()?.split(", ")?.map(::Instruction) ?: emptyList()
        val startPosition = Position()
        val finalPosition = instructions.fold(startPosition, Position::move)
        return startPosition.point.manhattanDistanceTo(finalPosition.point)
    }

    class Instruction(val turn: Turn, val magnitude: Int)
    {
        constructor(instruction: String) : this(Turn.fromChar(instruction[0]), instruction.substring(1).toInt())
    }

    class Position(val point: Point = Point(0, 0), val orientation: Orientation = Orientation.NORTH) {
        fun move(instruction: Instruction): Position {
            val newOrientation = orientation + instruction.turn

            val newLocation =
                    when (newOrientation) {
                        Orientation.NORTH -> Point(point.x, point.y + instruction.magnitude)
                        Orientation.EAST -> Point(point.x + instruction.magnitude, point.y)
                        Orientation.SOUTH -> Point(point.x, point.y - instruction.magnitude)
                        Orientation.WEST -> Point(point.x - instruction.magnitude, point.y)
                    }

            return Position(newLocation, newOrientation)
        }
    }

    class Point(val x: Int, val y: Int) {
        fun manhattanDistanceTo(point: Point): Int {
            return Math.abs(x - point.x) + Math.abs(y - point.y)
        }
    }

    enum class Orientation {
        NORTH, EAST, SOUTH, WEST;

        companion object { val values = Orientation.values() }

        operator fun plus(value: Turn): Orientation {
            return when (value) {
                Turn.RIGHT -> this + 1
                Turn.LEFT -> this - 1
            }
        }

        operator fun plus(value: Int): Orientation {
            val ordinal = (this.ordinal + value) % Orientation.values.count()
            return values[ordinal]
        }

        operator fun minus(value: Int): Orientation {
            val adjustedValue = Orientation.values.count() - (Math.abs(value) % Orientation.values.count())
            return plus(adjustedValue)
        }
    }

    enum class Turn {
        RIGHT, LEFT;

        companion object {
            fun fromChar(character: Char): Turn =
                when (character) {
                    'R' -> RIGHT
                    'L' -> LEFT
                    else -> throw IllegalArgumentException("$this does not denote a valid Turn")
                }
        }
    }
}
