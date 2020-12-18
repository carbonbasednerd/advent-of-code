import java.io.File

val classRanges = mutableMapOf<String, Pair<IntRange, IntRange>>()
val yourTicket = mutableListOf<Int>()
val nearbyTickets = mutableListOf<List<Int>>()

fun readTickets(fileName: String) {
    var parseDataType = 0
    File(fileName).forEachLine loopy@{
        when (it) {
            "" -> {
                return@loopy
            }
            "your ticket:" -> {
                parseDataType++
                return@loopy
            }
            "nearby tickets:" -> {
                parseDataType++
                return@loopy
            }
        }

        when (parseDataType) {
            0 -> {
                val keyvals = it.split(":")
                val ranges = keyvals[1].split("or")
                val firstRange = ranges[0].trim().split("-")
                val secondRange = ranges[1].trim().split("-")
                classRanges[keyvals[0]] = (firstRange[0].toInt()..firstRange[1].toInt()) to
                        (secondRange[0].toInt()..secondRange[1].toInt())
            }
            1 -> {
                yourTicket.addAll(it.split(",").map{m-> m.toInt()})
            }
            2 -> {
                nearbyTickets.add(it.split(",").map{m->  m.toInt()})
            }
        }
    }
}

// part one
fun findInvalidTickets(): Int {
    var invalidCount = 0
    nearbyTickets.forEach { ticket ->
        ticket.forEach tdloop@{ ticketData ->
            var found = false
            classRanges.forEach { range ->
                if (ticketData in range.value.first || ticketData in range.value.second) {
                    found = true
                    return@tdloop
                }
            }
            if (!found) {
                invalidCount += ticketData
            }
        }
    }
    return invalidCount
}

//part two
fun findInvalidTicketsAndRemove() {
    val tempTickets = nearbyTickets.toList()
    var linesRemoved = 0
    tempTickets.forEachIndexed { index, ticket ->
        ticket.forEach tdloop@{ ticketData ->
            var found = false
            classRanges.forEach { range ->
                if (ticketData in range.value.first || ticketData in range.value.second) {
                    found = true
                    return@tdloop
                }
            }
            if (!found) {
                nearbyTickets.removeAt(index-linesRemoved)
                linesRemoved++
            }
        }
    }
}

fun mapClasses(): Map<String, Int> {
    val end = nearbyTickets.first().size
    val numTickets = nearbyTickets.size
    var counter = 0
    var skipIndexes = mutableListOf<Int>()
    val mapping = mutableMapOf<String, Int>()
    while (counter < end) {
        var potentialClasses = classRanges.keys.filter{!mapping.contains(it)}.toMutableSet()
        if (counter in skipIndexes) {
            counter++
            continue
        }
        for (x in (0..numTickets-1)) {
            if (potentialClasses.size == 1) {
                break
            }
            val number = nearbyTickets[x][counter]
            val yourNumber = yourTicket[counter]

            for (c in classRanges.filter{ potentialClasses.contains(it.key) }) {
                if (!(yourNumber in c.value.first || yourNumber in c.value.second)) {
                    potentialClasses.remove(c.key)
                }
            }

            for (c in classRanges.filter{ potentialClasses.contains(it.key) }) {
                if (!(number in c.value.first || number in c.value.second)) {
                    potentialClasses.remove(c.key)
                }
            }
        }

        if (potentialClasses.size != 1) {
            counter++
        } else {
            mapping[potentialClasses.first()] = counter
            skipIndexes.add(counter)
            counter = 0
        }
        potentialClasses.clear()
    }
    return mapping
}

fun findDepartures(data: Map<String, Int>): Long {
    val locations = data.filter { it.key.contains("departure") }.values
    var total = 1L
    locations.forEach {
        total *= yourTicket[it].toLong()
    }
    return total
}

fun main() {
    readTickets("data/data_day16")
    println("Ticket scanning error rates ${findInvalidTickets()}")
    findInvalidTicketsAndRemove()
    println("Answer: ${findDepartures(mapClasses())}")
}