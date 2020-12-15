import java.io.File

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
data class Bus(val offset: Int, val bus: Long)

fun readBusTimes2(fileName: String): List<String> {
    val buffer = mutableListOf<String>()
    File(fileName).forEachLine {
        buffer.add(it)
    }
    return buffer
}

//had some help with this one :(
//https://todd.ginsberg.com/post/advent-of-code/2020/day13/
fun findBusPattern(busses: List<Bus>): Long {
    var step = busses.first().bus
    var time = 0L
    busses.drop(1).forEach { (offset, bus) ->
        while ((time + offset) % bus != 0L) {
            time += step
        }
        step *= bus // New Ratio!
    }
    return time
}

fun main() {
    val dataFile = "data/data_day13"

    readBusTimes(dataFile)
    println("Earliest Bus I can take is ${findEarliestBus()}")

    val input = readBusTimes2(dataFile)
    //this also was not mine - but I like it!
    val busses: List<Bus> = input.last().split(",").mapIndexedNotNull { index, s -> if (s == "x") null else Bus(index, s.toLong()) }
    println("Find bus pattern: ${findBusPattern(busses)}")
}