import java.io.File

fun readCode(fileName: String): MutableList<Pair<String, Int>> {
    val codeList = mutableListOf<Pair<String, Int>>()
    File(fileName).forEachLine {
        val splitData = it.split(" ")
        val instruction = splitData[0].trim()
        val value = splitData[1].trim().toInt()
        codeList.add(instruction to value)
    }
    return codeList
}

fun findInfiniteLoop(data: List<Pair<String, Int>>): Pair<Boolean, Int> {
    val instructionHistory = mutableSetOf<Int>()
    var accumulator = 0
    var currentPosition = 0
    while (!instructionHistory.contains(currentPosition) && currentPosition < data.size) {
        instructionHistory.add(currentPosition)
        val operation = data[currentPosition].first
        val operationValue = data[currentPosition].second
        when (operation) {
            "acc" -> {
                accumulator += operationValue
                currentPosition++
            }
            "jmp" -> {
                currentPosition += operationValue
            }
            else -> {
                currentPosition++
            }
        }
    }

    return (currentPosition == data.size) to accumulator
}

fun autoFixBuggyCode(data: MutableList<Pair<String, Int>>): Int {
    data.forEachIndexed {index, instruction ->
        when (instruction.first) {
            "nop" -> {
                val oldInstruction = data[index]
                data[index] = "jmp" to oldInstruction.second
                val result = findInfiniteLoop(data)
                if (result.first) {
                    return result.second
                } else {
                    data[index] = oldInstruction
                }
            }
            "jmp" -> {
                val oldInstruction = data[index]
                data[index] = "nop" to oldInstruction.second
                val result = findInfiniteLoop(data)
                if (result.first) {
                    return result.second
                } else {
                    data[index] = oldInstruction
                }
            }
        }
    }
    return -1
}

fun main() {
    println("Value of accumulator at start of loop: ${findInfiniteLoop(readCode("data/data_day8")).second}")
    println("Value of accumulator after fixing bug: ${autoFixBuggyCode(readCode("data/data_day8"))}")
}