import java.io.File

fun readLuggageData(fileName: String): Map<String, Map<String, Int>> {
    val luggageRules = mutableMapOf<String, Map<String, Int>>()
    File(fileName).bufferedReader().forEachLine {
        val splitData = it.split("contain")
        val mainKey = splitData[0].replace("bags", "").trim()
        val splitRules = splitData[1].split(",")
        val rules = mutableMapOf<String, Int>()
        splitRules.forEach {
            var bagType = it.replace("bag[s]?.".toRegex(), "").trim()
            var number = bagType.filter {x -> x.isDigit()}
            bagType = bagType.replace("$number ", "")
            rules[bagType] = if (number == "") 0 else number.toInt()
        }
        luggageRules[mainKey] = rules
    }

    return luggageRules
}

fun findProperBagsColorsForTransport(bagToPack: String, data: Map<String, Map<String, Int>>): Int {
    var validBags = 0
    data.forEach {bag ->
        if (bag.value.containsKey(bagToPack)) validBags++
    }
    return validBags
}

fun recursiveBagSearch(bagToPack: String, bagToCheck: String): Int {
    
}

fun main() {
    println(findProperBagsColorsForTransport("shiny gold", readLuggageData("data/test_data_day7")))
}
