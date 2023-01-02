import java.io.*

class Calculator { 
    fun readFileAsLines(fileName: String): List<String>
       = File(fileName).bufferedReader().readLines()
    /*...*/ 
}

fun main(@Suppress("UNUSED_PARAMETER") args: Array<String>) {
  println("Hello world!")

  val lines = Calculator().readFileAsLines("Example.txt")
  lines.forEach{it -> println("$it")};      
 
}

