import java.io.File
val seatData = mutableListOf<String>()

fun readSeatData(fileName: String) {
    File(fileName).forEachLine {
        seatData.add(it)
    }
}

fun hasAdjacentSeatStatus(seatStatus: Char, row: Int, col: Int, data: List<String>, advanced: Boolean = false): Int {
    return if (advanced) hasAdvancedAdjacentSeatStatus(seatStatus,row, col, data)
            else hasSimpleSeatStatus(seatStatus, row, col, data)
}


fun hasSimpleSeatStatus(seatStatus: Char, row: Int, col: Int, data: List<String>): Int {
    var matchedCounter = 0
    //check top
    if (row-1 >= 0) {
        val north = data[row-1][col]
        val northWest = if (col-1 >= 0) data[row-1][col-1] else 'X'
        val northEast = if (col+1 < data[row-1].length) data[row-1][col+1] else 'X'

        if (north == seatStatus) matchedCounter++
        if (northWest == seatStatus) matchedCounter++
        if (northEast == seatStatus) matchedCounter++
    }

    //Check middle
    val west = if (col-1 >= 0) data[row][col-1] else 'X'
    val east = if (col+1 < data[row].length) data[row][col+1] else 'X'
    if (west == seatStatus) matchedCounter++
    if (east == seatStatus) matchedCounter++

    //Check Bottom
    if (row+1 < data.size) {
        val south = data[row+1][col]
        val southWest = if (col-1 >= 0) data[row+1][col-1] else 'X'
        val southEast = if (col+1 < data[row+1].length) data[row+1][col+1] else 'X'

        if (south == seatStatus) matchedCounter++
        if (southWest == seatStatus) matchedCounter++
        if (southEast == seatStatus) matchedCounter++
    }

    return matchedCounter
}

fun hasAdvancedAdjacentSeatStatus(seatStatus: Char, row: Int, col: Int, data: List<String>): Int {
    var matchedCounter = 0

    //check north
    var rowIndex = row-1
    var found = false
    while (rowIndex >= 0 && !found) {
        if (data[rowIndex][col] == '#' || data[rowIndex][col] == 'L') {
            if (data[rowIndex][col] == seatStatus) {
                matchedCounter++
            }
            found = true
        }
        rowIndex--
    }

    //check south
    rowIndex = row+1
    found = false
    while (rowIndex < data.size && !found) {
        if (data[rowIndex][col] == '#' || data[rowIndex][col] == 'L') {
            if (data[rowIndex][col] == seatStatus) {
                matchedCounter++
            }
            found = true
        }
        rowIndex++
    }

    //check east
    var colIndex = col+1
    found = false
    while (colIndex < data[row].length && !found) {
        if (data[row][colIndex] == '#' || data[row][colIndex] == 'L') {
            if (data[row][colIndex] == seatStatus) {
                matchedCounter++
            }
            found = true
        }
        colIndex++
    }

    //check west
    colIndex = col-1
    found = false
    while (colIndex >= 0 && !found) {
        if (data[row][colIndex] == '#' || data[row][colIndex] == 'L') {
            if (data[row][colIndex] == seatStatus) {
                matchedCounter++
            }
            found = true
        }
        colIndex--
    }

    //check north east
    colIndex = col+1
    rowIndex = row-1
    found = false
    while (colIndex < data[row].length && rowIndex >= 0 && !found) {
        if (data[rowIndex][colIndex] == '#' || data[rowIndex][colIndex] == 'L') {
            if (data[rowIndex][colIndex] == seatStatus) {
                matchedCounter++
            }
            found = true
        }
        rowIndex--
        colIndex++
    }

    //check north west
    colIndex = col-1
    rowIndex = row-1
    found = false
    while (colIndex >= 0 && rowIndex >= 0 && !found) {
        if (data[rowIndex][colIndex] == '#' || data[rowIndex][colIndex] == 'L') {
            if (data[rowIndex][colIndex] == seatStatus) {
                matchedCounter++
            }
            found = true
        }
        rowIndex--
        colIndex--
    }


    //check south east
    colIndex = col+1
    rowIndex = row+1
    found = false
    while (colIndex < data[row].length && rowIndex < data.size && !found) {
        if (data[rowIndex][colIndex] == '#' || data[rowIndex][colIndex] == 'L') {
            if (data[rowIndex][colIndex] == seatStatus) {
                matchedCounter++
            }
            found = true
        }
        rowIndex++
        colIndex++
    }

    //check south west
    colIndex = col-1
    rowIndex = row+1
    found = false
    while (colIndex >= 0 && rowIndex < data.size && !found) {
        if (data[rowIndex][colIndex] == '#' || data[rowIndex][colIndex] == 'L') {
            if (data[rowIndex][colIndex] == seatStatus) {
                matchedCounter++
            }
            found = true
        }
        rowIndex++
        colIndex--
    }

    return matchedCounter
}

fun simulateSeating(dataList: MutableList<String>, neighborTolerance: Int = 4, useAdvancedSearch: Boolean = false): Pair<Int, Int> {
    val data = mutableListOf<String>()
    data.addAll(dataList)
    var bufferData = mutableListOf<String>()
    var changes = true
    var passes = 0
    while(changes) {
        bufferData.clear()
        changes = false
        data.forEachIndexed { row, rowData ->
            var tempRowData = ""
            rowData.forEachIndexed columnLoop@{ col, seat ->
                if (seat == '.') {
                    tempRowData+='.'
                    return@columnLoop
                }

                if (seat == 'L') {
                    if (hasAdjacentSeatStatus('#', row, col, data, useAdvancedSearch) == 0) {
                        tempRowData+='#'
                        changes = true
                    } else {
                        tempRowData+='L'
                    }
                    return@columnLoop
                }

                if (seat == '#') {
                    if (hasAdjacentSeatStatus('#', row, col, data, useAdvancedSearch) >= neighborTolerance) {
                        tempRowData+='L'
                        changes = true
                    }
                    else {
                        tempRowData+='#'
                    }
                    return@columnLoop
                }
            }
            bufferData.add(tempRowData)
        }
        passes++
        data.clear()
        data.addAll(bufferData)
    }

    return countSeats(data) to passes
}

fun countSeats(data: List<String>): Int = data.map { it.count { char-> char == '#' }}.sum()

fun main() {
    readSeatData("data/data_day11")
    var results = simulateSeating(seatData)
    println("Seat map complete: ${results.first} seats counted after ${results.second} passes")
    results = simulateSeating(seatData, 5, true)
    println("Seat map with new rules: ${results.first} seats counted after ${results.second} passes")
}