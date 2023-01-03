import java.io.*

data class Point(val x: Int, val y: Int) {
    companion object {
        fun fromLine(line: String): List<Point> =
            line.split(" -> ").map { it.split(",").let {
                Point(it[0].toInt(), it[1].toInt())
            }
        }
    }
}

class RockReader {
    fun readFileAsLines(fileName: String): List<String> =
            File(fileName).bufferedReader().readLines()
    
}

fun main() {
    val lines = RockReader().readFileAsLines("Example.txt")
    lines.forEach { println(Point.fromLine(it)) }
}
