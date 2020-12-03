import java.io.File

val HORIZONTAL_BASE_MOVE = 3

fun readTreeMap(fileName: String): List<String> {
    return File(fileName).readLines()
}

fun calculateNextIndex(currentCount: Int, queueLength: Int): Int {
    val nextMove = currentCount + HORIZONTAL_BASE_MOVE
    return if (queueLength - nextMove > 0) nextMove else (queueLength - nextMove) * -1
}

fun forestThroughTrees1(data: List<String>) {
    val queueLength = data[0].length
    var mutatingIndex = 0
    var treeCollisions = 0
    data.drop(1).forEach {
        mutatingIndex = calculateNextIndex(mutatingIndex, queueLength)
        if (it[mutatingIndex] == '#') {
            treeCollisions++
        }
    }

    println("Ouch! You hit $treeCollisions trees!")
}

fun main() {
    forestThroughTrees1(readTreeMap("data_day3"))
}