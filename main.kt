import java.io.*
import kotlin.math.*;

enum class Material {
    Air, Rock, Sand, Source
}

data class Point(val x: Int, val y: Int) {
    companion object {
        fun fromLine(line: String): List<Point> =
                line.split(" -> ").map { it.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }
    }
}

class RockMap {
    val rockHashMap: HashMap<Int, Material> = HashMap()

    init {
        addMaterial(500, 0, Material.Source)
    }

    fun addRockLines(points: List<Point>) {
        for (i in 0..(points.size - 2)) {
            addRockLine(points[i], points[i + 1])
        }
    }
    
    fun addRockLine(start: Point, end: Point) {
        if (start.x != end.x && start.y != end.y)
            throw Exception("Invalid rockline with $start and $end")

        if (start.x == end.x) {
            for (y in min(start.y, end.y)..max(start.y, end.y))
                addRock(start.x, y)
        } else {
            for (x in min(start.x, end.x)..max(start.x, end.x))
                addRock(x, start.y)
        }
    }
    
    fun addMaterial(x: Int, y: Int, material: Material) {
        rockHashMap.put(toIndex(x, y), material)
    }

    fun addRock(x: Int, y: Int) = addMaterial(x, y, Material.Rock)

    fun getMaterial(x: Int, y: Int): Material = rockHashMap.get(toIndex(x, y)) ?: Material.Air

    fun toIndex(x: Int, y: Int): Int = 1000 * x + y

    val size: Int // property type is optional since it can be inferred from the getter's return type
        get() = rockHashMap.size

    fun print(fromX: Int, toX: Int, toY: Int) {
        for (y in 0..toY) {
            for (x in fromX..toX) {
                val material = getMaterial(x, y)
                val c = when(material) {
                    Material.Air -> '.'
                    Material.Sand -> 'o'
                    Material.Rock -> '#'
                    Material.Source -> '+'                    
                }
                print(c)
            }
            println()
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

    val rockMap = RockMap()

    lines.forEach { rockMap.addRockLines(Point.fromLine(it)) }

    println("${rockMap.size}")

    rockMap.print(494, 503, 9)
}
