import java.io.File
import kotlin.math.pow

fun readProgram(fileName: String): List<String>
        = File(fileName).bufferedReader().readLines()

fun convertBinaryToDecimal(num: String): Long {
    var power = num.length
    var total = 0L
    num.forEach {
        power -= 1
        if (it == '1') {
            total += 2.0.pow(power).toLong()
        }
    }
    return total
}

fun pad(binaryString: String, length: Int): String = binaryString.padStart(length,'0')

// Part one
fun bitMaskingFun(data: List<String>): Long {
    val memory = mutableMapOf<Int, String>()

    var currentMask = data.first().split("=")[1]
    var registerSize = currentMask.length
    data.drop(1).forEach {
        val command = it.split("=")
        if (command[0].trim() == "mask") {
            currentMask = command[1]
            registerSize = currentMask.length
        } else {
            val memAddress = command[0].filter{ it.isDigit()}.toInt()
            val valueInBinary = Integer.toBinaryString(command[1].trim().toInt())
            memory[memAddress] = applyMask(currentMask, pad(valueInBinary, registerSize))
        }
    }

    var sum = 0L
    memory.forEach{
        sum += convertBinaryToDecimal(it.value)
    }
    return sum
}

fun applyMask(mask: String, stringToMask: String): String {
    var newString = StringBuffer(stringToMask)
    mask.forEachIndexed {index, c ->
        when(c) {
            '1' -> newString.setCharAt(index, '1')
            '0' -> newString.setCharAt(index, '0')
        }
    }
    return newString.toString()
}



// part two
fun bitMaskingWithAdapter(data: List<String>): Long {
    val memory = mutableMapOf<Long, Long>()

    var currentMask = data.first().split("=")[1]
    var registerSize = currentMask.length
    data.drop(1).forEach {
        val command = it.split("=")
        if (command[0].trim() == "mask") {
            currentMask = command[1]
            registerSize = currentMask.length
        } else {
            val memAddress = command[0].filter{ it.isDigit()}.toInt()
            val valueInBinary = Integer.toBinaryString(memAddress)
            val registerValue = command[1].trim().toLong()
            memory.apply {
                applyMaskV2(currentMask, pad(valueInBinary, registerSize)).forEach { buff ->
                    val decimalValue = convertBinaryToDecimal(buff)
                    put(decimalValue, registerValue)
                }
            }
        }
    }

    var sum = 0L
    memory.forEach{
        sum += it.value
    }
    return sum
}

fun applyMaskV2(mask: String, stringToMask: String): List<String> {
    val int = mask.count { it=='X' }
    val maxRegisters = 2.0.pow(int).toInt()
    val registers = (1..maxRegisters).map {
        val builder = StringBuilder()
        (1..mask.length).forEach{
            builder.append('0')
        }
        builder
    }
    var repeater = if (maxRegisters == 1) 1 else maxRegisters/2

    mask.forEachIndexed {index, c ->
        when(c) {
            '1' -> registers.forEach{ it.setCharAt(index, '1')}
            '0' -> registers.forEach{ it.setCharAt(index, stringToMask[index])}
            'X' -> {
                var counter = 0
                var flag = true
                registers.forEachIndexed {i, buff ->
                    if (counter == repeater) {
                        flag = !flag
                        counter = 0
                    }
                    counter++
                    when (flag) {
                        true -> { buff.setCharAt(index, '1')}
                        false -> { buff.setCharAt(index, '0')}
                    }
                }
                repeater /= 2
            }
        }
    }
    return registers.map{ it.toString()}
}

fun main() {
    val programData = readProgram("data/data_day14")
    println("Memory banks core value dump: ${bitMaskingFun(programData)}")
    println("V2 algorithmnessocity concurrency value beta mark 7: ${bitMaskingWithAdapter(programData)}")
}