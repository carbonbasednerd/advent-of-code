import java.io.File

fun readAndFormat(fileName: String): List<Triple<String, String, String>> {
    val intList = mutableListOf<Triple<String,String,String>>()
    File(fileName).forEachLine {
        val splitString = it.split(" ")
        intList.add(Triple(splitString[0], splitString[1].replace(":",""), splitString[2]))
    }

    return intList
}

fun part1PasswordCheck(data: List<Triple<String, String, String>>): Int {
    var validPasswordCounter = 0
    data.forEach { passwordData ->
        val counter = passwordData.third.count { it == passwordData.second[0]}
        val range = passwordData.first.split("-")
        if (counter in (range[0].toInt() .. range[1].toInt())){
            validPasswordCounter++
        }
    }
    return validPasswordCounter
}

fun part2PasswordCheck(data: List<Triple<String, String, String>>): Int {
    var validPasswordCounter = 0
    data.forEach { passwordData ->
        val indexes = passwordData.first.split("-")
        val passwordLength = passwordData.third.length
        var validPassword = false
        if (indexes[0].toInt() <= passwordLength && passwordData.third[indexes[0].toInt()-1] == passwordData.second[0]) {
            validPassword = !validPassword
        }
        if (indexes[1].toInt() <= passwordLength && passwordData.third[indexes[1].toInt()-1] == passwordData.second[0]) {
            validPassword = !validPassword
        }

        if (validPassword) {
            validPasswordCounter++
        }

    }
    return validPasswordCounter
}

fun main() {
    val passwordListAndData = readAndFormat("data/data_day2")
    println("${part1PasswordCheck(passwordListAndData)} Valid Passwords out of ${passwordListAndData.size} passwords")
    println("${part2PasswordCheck(passwordListAndData)} Valid Passwords out of ${passwordListAndData.size} passwords")
}