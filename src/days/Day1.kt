package days

import java.io.File
import java.util.*

class Day1 : Day {
    override operator fun invoke(input: File?) {
        println("Distance to Easter Bunny HQ: ${part1(input)} blocks")
        println("Actual distance to Easter Bunny HQ: ${part2(input)} blocks")
    }

    fun part1(input: File?): Double {
        val instructions = readInstructionsFromFile(input)
        val startPosition = Position()
        val finalPosition = instructions.fold(startPosition, Position::move)
        return startPosition.point.manhattanDistanceTo(finalPosition.point)
    }

    fun part2(input: File?): Double? {
        val instructions = readInstructionsFromFile(input)
        val lines = instructions.toLineList()
        val intersections = lines.perpendicularIntersections()
        val doubleVisit = intersections.firstOrNull()
        return lines.firstOrNull()?.start?.manhattanDistanceToOrNull(doubleVisit)
    }

    fun readInstructionsFromFile(input: File?): List<Instruction> {
        return input?.readText()?.split(", ")?.map(::Instruction) ?: emptyList()
    }

    fun List<Instruction>.toLineList(): List<Line> {
        val lines = ArrayList<Line>()
        var position = Position()
        this.forEach {
            val newPosition = position.move(it)
            lines.add(Line(position.point, newPosition.point))
            position = newPosition
        }
        return lines
    }

    fun List<Line>.perpendicularIntersections(): List<Point> {
        val points = LinkedList<Point>()
        this.forEachIndexed { i, line1 ->
            val insertIndex = points.size
            (0 until i - 1)
                    .map { this[it] }
                    .mapNotNull { line1.perpendicularIntersectionWith(it) }
                    .forEach {
                        if (insertIndex < points.size && it.manhattanDistanceTo(line1.start) < it.manhattanDistanceTo(points[insertIndex])) {
                            points.add(insertIndex, it)
                        } else {
                            points.addLast(it)
                        }
                    }
        }
        return points
    }

    class Instruction(val turn: Turn, val magnitude: Int)
    {
        constructor(instruction: String) : this(Turn.fromChar(instruction[0]), instruction.substring(1).toInt())
    }

    class Position(val point: Point = Point(0.0, 0.0), val orientation: Orientation = Orientation.NORTH) {
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

    class Point(val x: Double, val y: Double) {
        fun manhattanDistanceTo(point: Point): Double {
            return Math.abs(x - point.x) + Math.abs(y - point.y)
        }

        fun manhattanDistanceToOrNull(point: Point?): Double? {
            return if (point != null) manhattanDistanceTo(point) else null
        }
    }

    class Line(val start: Point, val end: Point) {
        fun perpendicularIntersectionWith(line: Line): Point? {
            val x: Double?
            val xBounds: ClosedRange<Double>?
            if (this.start.x == this.end.x) {
                x = this.start.x
                xBounds = line.start.x.rangeToOrReverse(line.end.x)
            } else if (line.start.x == line.end.x) {
                x = line.start.x
                xBounds = this.start.x.rangeToOrReverse(this.end.x)
            } else {
                x = null
                xBounds = null
            }

            val y: Double?
            val yBounds: ClosedRange<Double>?
            if (this.start.y == this.end.y) {
                y = this.start.y
                yBounds = line.start.y.rangeToOrReverse(line.end.y)
            } else if (line.start.y == line.end.y) {
                y = line.start.y
                yBounds = this.start.y.rangeToOrReverse(this.end.y)
            } else {
                y = null
                yBounds = null
            }

            val existAndInBounds = xBounds?.containsOrNull(x) ?: false && yBounds?.containsOrNull(y) ?: false
            return if (existAndInBounds) Point(x as Double, y as Double) else null
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

fun <T : Comparable<T>> ClosedRange<T>.containsOrNull(value: T?): Boolean? = if (value == null) null else this.contains(value)
fun <T : Comparable<T>> T.rangeToOrReverse(that: T): ClosedRange<T> = if (this <= that) this.rangeTo(that) else that.rangeTo(this)