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
data class Bus(val id: Int, val distance: Int, var times: MutableList<BigInteger> = mutableListOf(), var seed: BigInteger = BigInteger.ZERO) {
    fun generateTimes() {
        var count = seed
        for (x in (0..1000)) {
            count += id.toBigInteger()
            times.add(count)
        }
        seed = times.last()
    }

    fun check(time: BigInteger): Boolean {
        return times.contains(time+distance.toBigInteger())
    }

    fun cleanUp() {
        val middle = times.size / 2
        times = times.subList(0, middle)
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

fun findBusPattern(): BigInteger {
    var timeStamp = BigInteger.ZERO
    var checkVal = false
    var count = 0
    while (checkVal == false) {
        var report = true
        busses.forEach {
            it.generateTimes()
            if (report) {
                println(++count)
                report = false
            }
        }

        for (ts in busses.first().times) {
            val flagList = busses.map{BigInteger.ZERO}.toMutableList()
            busses.forEachIndexed { index, bus ->
                if (index != 0) {
                    if (bus.check(ts)) {
                        flagList[index] = ts
                    }
                } else {
                    flagList[index] = ts
                }
            }
            checkVal = !flagList.contains(BigInteger.ZERO)
            if (checkVal) {
                timeStamp = flagList.first()
                break
            }
        }
        busses.forEach { it.cleanUp() }
    }
    return timeStamp
}

fun main() {
    val dataFile = "data/test_data_day13"
//    readBusTimes(dataFile)
//    println("Earliest Bus I can take is ${findEarliestBus()}")

    readBusTimes2(dataFile)
    println(busses)
    println("Find bus pattern: ${findBusPattern()}")
}