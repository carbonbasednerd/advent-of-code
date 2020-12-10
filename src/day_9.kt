import java.io.File
import java.math.BigInteger

val data = mutableListOf<BigInteger>()

fun readNumbers(fileName: String): List<BigInteger> {
    File(fileName).forEachLine {
        data.add(BigInteger(it))
    }
    return data
}

fun checkValidity(range: Int): BigInteger {
    var invalidNumber = BigInteger.ZERO
    var index = range
    while (invalidNumber == BigInteger.ZERO && index < data.size) {
        val numberAtIndex = data[index]
        val rangedSubSet = data.subList(index - range, index)
        var foundValidNumber = false
        rangedSubSet.forEach {
            val numberToFind = numberAtIndex - it
            if (rangedSubSet.contains(numberToFind)) {
                foundValidNumber = true
            }
        }
        if (!foundValidNumber) {
            invalidNumber = numberAtIndex
        } else {
            index++
        }
    }
    return invalidNumber
}

fun findWeakness(number: BigInteger): BigInteger {
    var endRange = 0
    var index = 0
    while (endRange == 0 && index < data.size) {
        var total = data[index]
        var lookAheadIndex = index
        while (total < number && lookAheadIndex < data.size) {
            total += data[++lookAheadIndex]
        }
        if (total == number) {
            endRange = lookAheadIndex
        } else {
            index++
        }
    }
    val weakNumberRange = data.subList(index, endRange)
    return weakNumberRange.max()!!.plus(weakNumberRange.min()!!)
}

fun main() {
    readNumbers("data/data_day9")
    val nonConformistNumber = checkValidity(25)
    println("$nonConformistNumber does not conform!")
    println("X-MAS encrypted weakness is ${findWeakness(nonConformistNumber)}")
}