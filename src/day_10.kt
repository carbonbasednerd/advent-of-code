import java.io.File

val joltData = mutableListOf<Int>()
val joltDataSorted = mutableListOf<Int>()

fun readJolts(fileName: String) {
    File(fileName).forEachLine {
        joltData.add(it.toInt())
    }
}

fun testJoltAdapters(): Int {
    val sortedJoltData = joltData.sorted()
    var currentJolts = 0
    var differenceOfOne = 0
    var differenceOfThree = 1
    for (jolt in sortedJoltData) {
        val joltDiff = jolt - currentJolts
        if (joltDiff == 1) {
            currentJolts = jolt
            differenceOfOne++
        } else if (joltDiff == 2) {
            currentJolts = jolt
        } else if (joltDiff == 3) {
            currentJolts = jolt
            differenceOfThree++
        } else {
            //jolt diff too high. terminating
            break
        }
    }

    return differenceOfOne * differenceOfThree
}

fun testCombinations(): Int {
    val sortedJoltData = joltData.sorted()
    var paths = 1
    var index = 0
    for (jolt in sortedJoltData) {
        //look ahead and see if the next potential 3 values are there
        val joltRange = (jolt + 1..jolt + 3)
        val lookAheadList = mutableListOf<Int>()
        var parseLastBit = false
        var hasOutsideRange = false
        for (x in (1..4)) {
            if (sortedJoltData.size <= index+x) {
                parseLastBit = true
                break
            } else if (sortedJoltData[index + x] in joltRange) {
                lookAheadList.add(sortedJoltData[index + x])
            } else {
                hasOutsideRange = true
                lookAheadList.add(sortedJoltData[index + x])
                break
            }
        }

        if (parseLastBit) {
            when(lookAheadList.size) {
                3 -> {
                    when (lookAheadList[0] + 3) {
                        lookAheadList[2] -> paths += 2
                        else -> paths++
                    }
                }
            }

        } else {
            when(lookAheadList.size) {
                3 -> {
                    when (lookAheadList[0]+3) {
                        lookAheadList[2] -> paths += 3
                        else -> paths++
                    }
                }
                4 -> {
                    when (lookAheadList[0]+3) {
                        lookAheadList[3] -> {
                            paths += 6
                        }
                        lookAheadList[3]-1 -> {
                            paths += 5
                        }
                        lookAheadList[3]-2 -> {
                            paths += 4
                        }

                    }
                }
            }
        }
        index++

    }
    return paths + 3
}

fun testCombinations3(start: Int): Int {
    var counter = 0
    for (x in (start..(joltDataSorted.size -1))) {
        val joltRange = (joltDataSorted[x]+1..joltDataSorted[x]+3)
        val branches = mutableListOf<Int>()
        for (y in (1..3)) {
            if (x+y >= joltDataSorted.size) break
            if (joltDataSorted[x+y] in joltRange){
                branches.add(x+y)
//                counter += testCombinations3(x+y)
            }
        }
        if (branches.size > 1) {
            for (z in (1..(branches.size -1))) {
                counter += testCombinations3(branches[z])
            }
        }
    }

    return counter+1
}

fun testCombinations2(): Int {
    val sortedJoltData = joltData.sorted()
    var paths = 1
    var index = 0
    var depth = 0
    var branches = 1
    for (jolt in sortedJoltData) {
        //look ahead and see if the next potential 3 values are there
        val joltRange = (jolt + 1..jolt + 3)
        var count = 0
        for (x in (1..3)) {
            if (index + x >= sortedJoltData.size) break
            if (sortedJoltData[index + x] in joltRange){
                count++
            }
        }

        if (count > 1) {
            paths += count
        }

        index++
        depth++
    }

    return paths
}


fun main() {
    readJolts("data/test_data_day10_1")
    println("Result of jolt testing is ${testJoltAdapters()}")
    joltDataSorted.addAll(joltData.sorted())
    println("Number of permutations ${testCombinations2()}")
    println("Number of permutations ${testCombinations3(0)}")
}
