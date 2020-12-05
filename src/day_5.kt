import java.io.File


fun readBoardingPasses(fileName: String): List<String> {
    return File(fileName).readLines()
}

fun numberCruncher(value: Char, index: Int, buffer: Int): Pair<Int, Int> {
    var newIndex = 0
    var newBuffer = 0

    when(value) {
        'F', 'L' -> {
            newIndex = index/2
            newBuffer = buffer
        }
        'B', 'R' -> {
            newIndex = index/2
            newBuffer = buffer + index/2
        }
    }
    return newIndex to newBuffer
}

fun calculateSeatIds(data: List<String>): List<Int> {
    val seatIds = mutableListOf<Int>()
    data.forEach {
        var index = 128
        var buffer = 0
        it.substring(0..6).forEach {character ->
            val result = numberCruncher(character, index, buffer)
            index = result.first
            buffer = result.second
        }
        val row = (index-1) + buffer

        index = 8
        buffer = 0
        it.substring(7..9).forEach {character ->
            val result = numberCruncher(character, index, buffer)
            index = result.first
            buffer = result.second
        }
        val column = (index -1) + buffer

        seatIds.add((row * 8)+column)
    }
    return seatIds
}

fun findMySeat(data: List<Int>): Int {
    val sortedData = data.sorted()
    var mySeat = 0
    var seatCounter = sortedData[0]
    var loopCounter = 0
    while(mySeat == 0 || loopCounter >= sortedData[sortedData.size-1]) {
        if (sortedData[loopCounter] != seatCounter) {
            mySeat = seatCounter
        }
        seatCounter++
        loopCounter++
    }

    return mySeat
}

fun main() {
    val seatIds = calculateSeatIds(readBoardingPasses("data_day5"))
    println("Largest Seat: ${seatIds.max()}")
    println("Found my seat in the weirdest way possible! Number ${findMySeat(seatIds)}")
}