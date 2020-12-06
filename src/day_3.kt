import java.io.File

var horizontalBaseMove = 3
var verticalBaseMove = 1

fun readTreeMap(fileName: String): List<String> {
    return File(fileName).readLines()
}

fun calculateNextIndex(currentCount: Int, queueLength: Int): Int {
    val nextMove = currentCount + horizontalBaseMove
    return if (queueLength - nextMove > 0) nextMove else (queueLength - nextMove) * -1
}

fun forestThroughTrees1(data: List<String>): Int {
    val queueLength = data[0].length
    var mutatingIndex = 0
    var treeCollisions = 0
    for (i in 0 until data.size step verticalBaseMove) {
        mutatingIndex = calculateNextIndex(mutatingIndex, queueLength)
        if (data[i][mutatingIndex] == '#') {
            treeCollisions++
        }
    }

    return treeCollisions
}

fun forestThroughTrees2(data: List<String>): Int {
    val scenarios = listOf(1 to 1, 3 to 1, 5 to 1, 7 to 1, 1 to 2)
    var treeCollisions = 1
    scenarios.forEach {
        horizontalBaseMove = it.first
        verticalBaseMove = it.second
        treeCollisions *= forestThroughTrees1(data.drop(verticalBaseMove))
    }
    
    return treeCollisions
}

fun main() {
    println("Ouch! You hit ${forestThroughTrees1(readTreeMap("data/data_day3").drop(verticalBaseMove))} trees!")
    println("Ouch! You hit ${forestThroughTrees2(readTreeMap("data/data_day3"))} trees!")
}