import java.io.File


fun readIt(fileName: String): List<Int> {
    val intList = mutableListOf<Int>()
    File(fileName).forEachLine {
        intList.add(it.toInt())
    }

    return intList
}

fun getPair() {
    var magicOutput = -1
    val magicNum = 2020
    val data = readIt("data/data_day1").sortedDescending()

    data.takeWhile { magicOutput == -1 }.forEach first@{ firstNum ->
        val numToFind = magicNum - firstNum
        if (data.contains(numToFind)) {
            magicOutput = numToFind * firstNum
        } else {
            return@first
        }
    }
    println(magicOutput)
}

fun getTriple() {
    var magicOutput = -1
    val magicNum = 2020
    val data = readIt("data/data_day1").sortedDescending()
    val reversedData = data.reversed()

    data.takeWhile { magicOutput == -1 }.forEach first@{ firstNum ->
        val firstNumMax = magicNum - firstNum
        reversedData.forEach second@{ secondNum ->
            val secondNumToFind = firstNumMax - secondNum
            if (secondNum > firstNumMax) {
                return@first
            } else {
                if (data.contains(secondNumToFind)) {
                    magicOutput = firstNum * secondNum * secondNumToFind
                    return@first
                } else {
                    return@second
                }
            }
        }
    }
    println(magicOutput)
}


fun main() {
    getPair()
    getTriple()
}