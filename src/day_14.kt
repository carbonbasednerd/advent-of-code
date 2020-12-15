import java.io.File

fun readProgram(fileName: String): List<String>
        = File(fileName).bufferedReader().readLines()

fun main() {

}