import java.io.File

fun readPassportData(fileName: String): List<Map<String, String>> {
    val passports = mutableListOf<Map<String, String>>()
    var tempPassportData = mutableMapOf<String, String>()
    File(fileName).forEachLine  reading@{
        if (it == "" || it == "\n") {
            if (tempPassportData.size > 0) {
                passports.add(tempPassportData)
            }
            tempPassportData = mutableMapOf()
            return@reading
        }

        if (it.contains(" ")) {
            val keyValuePairs = it.split(" ")
            keyValuePairs.forEach {
                val kvp = it.split(":")
                if (kvp[1] == "") println("space ${kvp[1]}")
                tempPassportData[kvp[0]] = kvp[1]
            }
        } else {
            val kvp = it.split(":")
            tempPassportData[kvp[0]] = kvp[1]
        }
    }
    return passports
}

fun checkForValidPassports(data: List<Map<String, String>>): Pair<Int, Int> {
    val validFields = arrayListOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    var validPassport = 0
    var invalidPassports = 0
    data.forEach mapLoop@{ dataMap ->
        validFields.forEach {  key ->
            if (!dataMap.containsKey(key)) {
                invalidPassports++
                return@mapLoop
            }
        }
        validPassport++
    }
    return validPassport to invalidPassports
}

fun checkForValidPassports2(data: List<Map<String, String>>): Pair<Int, Int> {
    val validFields = arrayListOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid")
    var validPassport = 0
    var invalidPassports = 0
    data.forEach mapLoop@{ dataMap ->
        validFields.forEach {  key  ->
            if (dataMap.containsKey(key)) {
                var isValid = true
                when(key) {
                    "byr"-> {
                        if (dataMap[key]?.toInt() !in (1920..2002)) isValid = false
                    }
                    "iyr" -> {
                        if (dataMap[key]?.toInt() !in (2010..2020)) isValid = false
                    }
                    "eyr" -> {
                        if (dataMap[key]?.toInt() !in (2020..2030)) isValid = false
                    }
                    "hgt" -> {
                        if (dataMap[key]?.contains("in") == true) {
                            val value = dataMap[key]?.substringBefore("in")?.toInt()
                            if (value !in (59..76)) isValid = false
                        } else if (dataMap[key]?.contains("cm") == true) {
                            val value = dataMap[key]?.substringBefore("cm")?.toInt()
                            if (value !in (150..193)) isValid = false
                        }
                    }
                    "hcl" -> {
                        if (regExMatch("^#[0-9 a-f]{6}".toRegex(), dataMap[key]!!)) isValid = false
                    }
                    "ecl" -> {
                        if (regExMatch("^(amb|blu|brn|gry|grn|hzl|oth)".toRegex(), dataMap[key]!!)) isValid = false
                    }
                    "pid" -> {
                        if (regExMatch("^[0-9]{9}".toRegex(), dataMap[key]!!)) isValid = false
                    }
                }
                if (!isValid) {
                    invalidPassports++
                    return@mapLoop
                }
            } else {
                invalidPassports++
                return@mapLoop
            }
        }
        validPassport++
    }
    return validPassport to invalidPassports
}

fun regExMatch(rex: Regex, data: String): Boolean {
    val matcher = rex.matchEntire(data)
    return matcher == null
}

fun main() {
    var returnedData = checkForValidPassports(readPassportData("data_day4"))
    println("${returnedData.first} valid passports and ${returnedData.second} invalid passports")
    returnedData = checkForValidPassports2(readPassportData("data_day4"))
    println("${returnedData.first} valid passports and ${returnedData.second} invalid passports")
}
