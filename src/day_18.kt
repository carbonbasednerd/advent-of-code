import java.io.File

val problems = mutableListOf<List<String>>()

fun readMathProblems(fileName: String) {
    var numBuff = ""
    File(fileName).forEachLine {
        val tempList = mutableListOf<String>()
        it.forEach { c ->
            if (c.isDigit()) {
                numBuff += c
            } else {
                when (c) {
                    ' ' -> {
                        if (numBuff != "") {
                            tempList.add(numBuff)
                            numBuff = ""
                        }
                    }
                    else -> {
                        if (numBuff != "") {
                            tempList.add(numBuff)
                            numBuff = ""
                        }
                        tempList.add(c.toString())
                    }
                }
            }
        }
        if (numBuff.isNotEmpty()) {
            tempList.add(numBuff)
            numBuff = ""
        }
        problems.add(tempList)
    }
}

//part 1
fun solution1(data: List<List<String>>): Long {
    var total = 0L
    data.forEach {
        val result = solver(0, it)
        total += result.second
    }
    return total
}

fun solver(count: Int, data: List<String>): Pair<Int, Long> {
    var total = 0L
    var numBuff = mutableListOf<Long>()
    var operator = ""
    var counter = count

    while (counter < data.size) {
        if (data[counter] == "+" || data[counter] == "*") {
            operator = data[counter]
        } else if (data[counter] == "(") {
            val result = solver(0, data.subList(counter + 1, data.size))
            counter += result.first + 1
            numBuff.add(result.second)
        } else if (data[counter] == ")") {
            return counter to total
        } else {
            numBuff.add(data[counter].toLong())
        }

        if (numBuff.isNotEmpty() && operator.isEmpty()) {
            total = numBuff.first()
            numBuff.clear()
        } else if (numBuff.isNotEmpty() && operator.isNotEmpty()) {
            when (operator) {
                "+" -> {
                    total += numBuff.first()
                }
                "*" -> {
                    total *= numBuff.first()
                }
            }
            numBuff.clear()
            operator = ""
        }
        counter++
    }

    return counter to total
}

//part 2

fun solution2(data: List<List<String>>): Long {
    var total = 0L
    data.forEach {
        val result = solver2(0, it)
        total += result.second
    }
    return total
}

fun solver2(count: Int, data: List<String>): Pair<Int, Long> {
    var total = 0L
    val numBuff = mutableListOf<Long>()
    val operator = mutableListOf<String>()
    var counter = count

    while (counter < data.size) {
        if (data[counter] == "+" || data[counter] == "*") {
            operator.add(data[counter])
        } else if (data[counter] == "(") {
            val result = solver2(0, data.subList(counter + 1, data.size))
            counter += result.first + 1
            numBuff.add(result.second)
        } else if (data[counter] == ")") {
            numBuff.forEach {
                total *= it
            }
            return counter to total
        } else {
            numBuff.add(data[counter].toLong())
        }

        if (numBuff.isNotEmpty() && operator.isEmpty()) {
            total = numBuff.first()
            numBuff.clear()
        } else if (numBuff.isNotEmpty() && operator.isNotEmpty() && operator.size == numBuff.size) {
            when (operator.last()) {
                "+" -> {
                    if (numBuff.size == 1) {
                        total += numBuff.first()
                        numBuff.clear()
                        operator.clear()
                    } else {
                        val tempTotal = numBuff.last() + numBuff[numBuff.size - 2]
                        val tempBuff = mutableListOf<Long>()
                        tempBuff.addAll(numBuff.dropLast(2))
                        numBuff.clear()
                        numBuff.addAll(tempBuff)
                        numBuff.add(tempTotal)
                        operator.removeAt(operator.lastIndex)
                    }


                }
            }
        }
        counter++
    }

    if (total == 0L) total = 1L
    numBuff.forEach {
        total *= it
    }
    return counter to total
}

fun main() {
    readMathProblems("data/data_day18")
    println("Calculatron says: the answer is ${solution1(problems)}")
    println("It's the new Math that gets ya: solution is ${solution2(problems)}")
}