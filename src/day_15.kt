import java.io.File

fun readSeed(fileName: String): List<String>
        = File(fileName).bufferedReader().readLines()

fun numberGame(data: String, terminateAt: Int): Int {
    val dataSplit = data.split(",")
    val tracking = mutableMapOf<Int, Int>()
    var counter = 1
    var lastNum = -1
    dataSplit.forEach {
        if (lastNum == -1) {
            lastNum = it.toInt()
        } else {
            tracking[lastNum] = counter-1
            lastNum = it.toInt()
        }
        counter++
    }

    while (counter <= terminateAt) {
        val isMapped = tracking.containsKey(lastNum)
        when (isMapped) {
            true -> {
                val lastSeen = tracking[lastNum]!!
                if ((counter - lastSeen) == 1) {
                    tracking[lastNum] = counter-1
                    lastNum = 0
                } else {
                    tracking[lastNum] = counter-1
                    lastNum = (counter-1) - lastSeen
                }
            }
            false -> {
                tracking[lastNum] = counter -1
                lastNum = 0
            }
        }
        counter++
    }
    return lastNum
}

fun numberGame2(data: String, terminateAt: Int): Int {
    val dataSplit = data.split(",")
    val tracking = mutableMapOf<Int, Int>()
    var counter = 1
    var lastNum = -1
    dataSplit.forEach {
        if (lastNum == -1) {
            lastNum = it.toInt()
        } else {
            tracking[lastNum] = counter-1
            lastNum = it.toInt()
        }
        counter++
    }

    while (counter-1 != terminateAt) {
        val isMapped = tracking.containsKey(lastNum)
        when (isMapped) {
            true -> {
                val lastSeen = tracking[lastNum]!!
                if ((counter - lastSeen) == 1) {
                    tracking[lastNum] = counter-1
                    lastNum = 0
                } else {
                    tracking[lastNum] = counter-1
                    lastNum = (counter-1) - lastSeen
                }
            }
            false -> {
                tracking[lastNum] = counter -1
                lastNum = 0
            }
        }
        counter++
    }
    return lastNum
}

fun main() {
    val seedNumbers = readSeed("data/data_day15")
    println("Bored! Last number was: ${numberGame(seedNumbers[0], 2020)}")
    println("The 30000000 in the game is ${numberGame2(seedNumbers[0], 30000000)}")

}