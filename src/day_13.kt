import java.io.File
import java.math.BigInteger

var timeToLeave: Int = 0
var busRoutes = mutableListOf<Int>()

fun readBusTimes(fileName: String) {
    var header = true
    File(fileName).forEachLine {
        if (header) {
            timeToLeave = it.toInt()
            header = false
        } else {
            val parsedData = it.split(',')
            busRoutes.addAll(parsedData.filter { entry -> entry != "x" }.map { item -> item.toInt() })
        }

    }
}

// Part one
fun findEarliestBus(): Int {
    val busTiming = mutableMapOf<Int, Int>()
    busRoutes.forEach {
        var counter = 0
        while (counter <= timeToLeave) {
            counter += it
        }
        busTiming[it] = counter - timeToLeave
    }
    val earliestAvailable = busTiming.minBy {
        it.value
    }
    return earliestAvailable!!.key * earliestAvailable.value
}

//part 2
data class Bus(val id: Int, val distance: Int, val times: MutableList<BigInteger> = mutableListOf(), var seed: BigInteger = BigInteger.ZERO) {
    fun generateTimes() {
        var count = seed
        for (x in (0..1000)) {
            count += id.toBigInteger()
            times.add(count)
        }
        seed = times.last()
    }

    fun check(time: BigInteger) {
        
    }
}

val busses = mutableListOf<Bus>()

fun readBusTimes2(fileName: String) {
    var header = true
    File(fileName).forEachLine {
        if (header) {
            timeToLeave = it.toInt()
            header = false
        } else {
            val parsedData = it.split(',')
            parsedData.forEachIndexed { index, s ->
                if (s != "x") {
                    busses.add(Bus(s.toInt(), index))
                }
            }
        }
    }
}

val a = Pair(17, 0)
val b = Pair(13, 2)
val c = Pair(19, 3)

//val d = Pair(31, 6)
//val e = Pair(19, 7)
var aCounter = mutableListOf<Int>()
var bCounter = mutableListOf<Int>()
var cCounter = mutableListOf<Int>()
var dCounter = 0
var eCounter = 0

fun findBusPattern(): Int {
    var checkVal = -1
    while (checkVal == -1) {
        busses.forEach {
            it.generateTimes()
        }

        busses.first().times.forEach {
            busses.forEachIndexed { index, bus ->
                if (index != 0) {
                    it.check(it)
                }
            }
        }
    }
    return checkVal
}

fun check(): Int {
    var found = -1
    println("Checking ${aCounter.first()} and ${aCounter.last()}")
    for (value in aCounter) {
        var flag = true
        if (!bCounter.contains(value + 2)) {
            flag = false
        }
        if (!cCounter.contains(value + 3)) {
            flag = false
        }
        if (flag) {
            found = value
            break
        }
    }
    return found
}

//fun counterCheck(): Boolean {
//    var flag = true
//    if (bCounter-aCounter != 1) {
//        flag = false
//    }
//    if (cCounter-aCounter != 4) {
//        flag = false
//    }
//    if (dCounter-aCounter != 6) {
//        flag = false
//    }
//    if (eCounter-aCounter != 7) {
//        flag = false
//    }
//    return flag
//}

fun main() {
    val dataFile = "data/test_data_day13"
    readBusTimes(dataFile)
    println("Earliest Bus I can take is ${findEarliestBus()}")

    readBusTimes2(dataFile)
    println(busses)
//    println("Find bus patter: ${findBusPattern()}")
}