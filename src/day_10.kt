import java.io.File
val joltData = mutableListOf<Long>()

fun readJolts(fileName: String) {
    File(fileName).forEachLine {
        joltData.add(it.toLong())
    }
}

fun testJoltAdapters(): Long {
    val sortedJoltData = joltData.sorted()
    var currentJolts = 0L
    var differenceOfOne = 0L
    var differenceOfThree = 1L
    for (jolt in sortedJoltData) {
        val joltDiff = jolt - currentJolts
        if (joltDiff == 1L) {
            currentJolts = jolt
            differenceOfOne++
        } else if (joltDiff == 2L) {
            currentJolts = jolt
        } else if (joltDiff == 3L) {
            currentJolts = jolt
            differenceOfThree++
        } else {
            //jolt diff too high. terminating
            break
        }
    }

    return differenceOfOne * differenceOfThree
}

fun testCombinations(): Long {
    var sortedJoltData = joltData.sorted()

    val children = mutableMapOf<Long, Long>()
    val joltRange = (1..3)
    for (x in (0..2)) {
        if (x >= sortedJoltData.size) break
        if (sortedJoltData[x] in joltRange){
            children[sortedJoltData[x]] = 1
        }
    }
    sortedJoltData.forEachIndexed { index, i ->
        val joltRange = ( i+1..i+3)
        for (x in (1..3)) {
            if (index + x >= sortedJoltData.size) break
            if (sortedJoltData[index + x] in joltRange){
                if (children.containsKey(sortedJoltData[index + x])) {
                    children.put(sortedJoltData[index + x], children[sortedJoltData[index + x]]!!+children[i]!!)
                } else {
                    children[sortedJoltData[index + x]] = children[i]!!
                }
            }
        }
    }
    return children.values.max()!!
}

fun main() {
    readJolts("data/data_day10")
    println("Result of jolt testing is ${testJoltAdapters()}")
    println("Find all combinations ${testCombinations()}")

}
