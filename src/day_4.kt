import java.io.File



fun readPassportData(fileName: String): List<List<Pair<String, String>>> {
    val passports = mutableListOf<List<Pair<String, String>>>()
    var tempPassportData = mutableListOf<Pair<String, String>>()
    File(fileName).forEachLine  reading@{
        if (it == "") {
            passports.add(tempPassportData)
            tempPassportData = mutableListOf<Pair<String, String>>()
            return@reading
        }

        if (it.contains(" ")) {
            val keyValuePairs = it.split(" ")
            keyValuePairs.forEach {
                val kvp = it.split(":")
                tempPassportData.add(kvp[0] to kvp[1])
            }
        } else {
            val kvp = it.split(":")
            tempPassportData.add(kvp[0] to kvp[1])
        }
    }

    return passports
}

fun checkForValidPassports(data: List<List<Pair<String, String>>>) {
    val validFields = arrayListOf("byr", "iyr")

    //todo maybe instead of a list this should be a map? Instead of the pairs? then I could just check if the key exists

}

fun main() {
    checkForValidPassports(readPassportData("data_day4"))
}
