import java.io.*
import kotlin.math.*

enum class Material {
    Air,
    Rock,
    Sand,
    Source,
    Nothingness
}

data class Point(val x: Int, val y: Int) {
    val nextMoves: List<Point> 
        get() = listOf(Point(x, y+1), Point(x-1, y+1), Point(x+1, y+1))
    

    val inTheVoid: Boolean
        get() = x == theVoid.x && y == theVoid.y
    
    companion object {
        fun fromLine(line: String): List<Point> =
                line.split(" -> ").map { it.split(",").let { Point(it[0].toInt(), it[1].toInt()) } }

        val theVoid = Point(-1, -1)
    }
}

class RockMap {
    val rockHashMap: HashMap<Int, Material> = HashMap()
    val sourcePos = Point(500, 0)
    var maxY = sourcePos.y
    var minX = sourcePos.x
    var maxX = sourcePos.x

    init {
        addMaterial(sourcePos.x, sourcePos.y, Material.Source)
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
            for (y in min(start.y, end.y)..max(start.y, end.y)) addRock(start.x, y)
        } else {
            for (x in min(start.x, end.x)..max(start.x, end.x)) addRock(x, start.y)
        }
    }

    fun addMaterial(x: Int, y: Int, material: Material) {
        maxY = max(maxY, y)
        minX = min(minX, x)
        maxX = max(maxX, x)

        rockHashMap.put(toIndex(x, y), material)
    }

    fun addSand(): Boolean { 
        val restPosition = getRestPosition(sourcePos)
        
        if (restPosition != null) {
            addMaterial(restPosition.x, restPosition.y, Material.Sand)
            return false
        }

        return true
    }

    fun getRestPosition(curr: Point): Point? {
        for (next in curr.nextMoves) {
            val nextMaterial = getMaterial(next.x, next.y)
            if (nextMaterial == Material.Nothingness) 
                return null

            if (nextMaterial == Material.Air)
                return getRestPosition(next)            
        }

        return curr
    }

    fun addRock(x: Int, y: Int) = addMaterial(x, y, Material.Rock)

    fun getMaterial(x: Int, y: Int): Material {
        if (y > maxY) return Material.Nothingness

        return rockHashMap.get(toIndex(x, y)) ?: Material.Air
    }

    fun toIndex(x: Int, y: Int): Int = 1000 * x + y

    val size:
            Int // property type is optional since it can be inferred from the getter's return type
        get() = rockHashMap.size

    fun print(fromX: Int, toX: Int, toY: Int) {
        for (y in 0..toY) {
            for (x in fromX..toX) {
                val material = getMaterial(x, y)
                val c =
                        when (material) {
                            Material.Air -> '.'
                            Material.Sand -> 'o'
                            Material.Rock -> '#'
                            Material.Source -> '+'
                            Material.Nothingness -> 'N'
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
    val lines = RockReader().readFileAsLines("Input.txt")
    lines.forEach { println(Point.fromLine(it)) }

    val rockMap = RockMap()

    lines.forEach { rockMap.addRockLines(Point.fromLine(it)) }

    println("${rockMap.size}")

    var isFinished = false
    var i = 0
    while (!isFinished) {
        isFinished = rockMap.addSand()

        if (!isFinished) {
            i++
            println(i)
        }
    }

    println("Finished at $i")
    
    // rockMap.print(rockMap.minX, rockMap.maxX, rockMap.maxY)
}
