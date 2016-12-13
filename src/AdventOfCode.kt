import days.*
import java.io.File

fun main(args: Array<String>) {
    println("Specify a day from 1-25 to view the solution for that day.")
    print("Day: ")
    val day = readLine()
    val dayClass = getDayClass(day) ?: Day0()
    val dayInput = getDayInputFile(day)
    dayClass(dayInput)
}

fun getDayClass(day: String?): Day? {
    return try { Class.forName("days.Day$day")?.newInstance() as? Day } catch (e: ClassNotFoundException) { return null }
}

fun getDayInputFile(day: String?): File? {
    val file = File("input/Day$day.txt")
    return if (file.isFile) file else null
}