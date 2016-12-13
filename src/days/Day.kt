package days

import java.io.File

interface Day {
    operator fun invoke(input: File? = null)
}