import java.io.File

fun readCustomsData(fileName: String): List<List<String>> {
    val groups = mutableListOf<List<String>>()
    var tempCustomsData = mutableListOf<String>()
    File(fileName).bufferedReader().forEachLine  reading@{
        if (it == "" || it == "\n"){
            if (tempCustomsData.size > 0) {
                groups.add(tempCustomsData)
            }
            tempCustomsData = mutableListOf()
            return@reading
        }

        tempCustomsData.add(it)
    }
    if (tempCustomsData.size > 0) {
        groups.add(tempCustomsData)
    }
    return groups
}

fun countCustomsEntry(data: List<List<String>>): Int{
    var totalYes = 0
    data.forEach {group ->
        val entrySet = mutableSetOf<Char>()
        group.forEach {entry ->
            entry.forEach {
                entrySet.add(it)
            }
        }
        totalYes += entrySet.count()
    }
    return totalYes
}

fun countCustomsEntryPartB() {
    
}

fun main() {
    println("Total yes entries: ${countCustomsEntry(readCustomsData("data/data_day6"))}")
}