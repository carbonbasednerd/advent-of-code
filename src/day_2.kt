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
    var passwordValidCounter = 0
    data.forEach { passwordData ->
        val counter = passwordData.third.count { it == passwordData.second[0]}
        val range = passwordData.first.split("-")
        if (counter in (range[0].toInt() .. range[1].toInt())){
            passwordValidCounter++
        }
    }
    return passwordValidCounter
}

fun part2PasswordCheck(data: List<Triple<String, String, String>>): Int {
    val validPasswordCounter = 0
    return validPasswordCounter
}

fun main() {
    val passwordListAndData = readAndFormat("data_day2")
    println("${part1PasswordCheck(passwordListAndData)} Valid Passwords out of ${passwordListAndData.size} passwords")
    println("${part2PasswordCheck(passwordListAndData)} Valid Passwords out of ${passwordListAndData.size} passwords")
}