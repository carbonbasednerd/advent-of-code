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

fun countCustomsEntryPart2(data: List<List<String>>): Int {
    var totalYes = 0
    data.forEach {group ->
        val groupTotal = group.size
        val entrySet = mutableMapOf<Char, Int>()
        group.forEach {entry ->
            entry.forEach {
                var counter = entrySet.getOrPut(it, {0})
                entrySet[it] = ++counter
            }
        }
        entrySet.forEach {
            if (it.value == groupTotal) {
                totalYes++
            }
        }
    }
    return totalYes
}

fun main() {
    val data = readCustomsData("data/data_day6")
    println("Total yes entries: ${countCustomsEntry(data)}")
    println("Total all yes entries: ${countCustomsEntryPart2(data)}")
}