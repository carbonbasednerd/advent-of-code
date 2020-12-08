import java.io.File

var luggageRules: Map<String, Map<String, Int>> = mutableMapOf()
var validGoldCarryingBags = mutableSetOf<String>()

fun readLuggageData(fileName: String): Map<String, Map<String, Int>> {
    val luggageRules = mutableMapOf<String, Map<String, Int>>()
    File(fileName).bufferedReader().forEachLine {
        val splitData = it.split("contain")
        val mainKey = splitData[0].replace("bags", "").trim()
        val splitRules = splitData[1].split(",")
        val rules = mutableMapOf<String, Int>()
        splitRules.forEach {
            var bagType = it.replace("bag[s]?.?".toRegex(), "").trim()
            var number = bagType.filter {x -> x.isDigit()}
            if (number != "") {
                bagType = bagType.replace("$number ", "")
            }
            rules[bagType] = if (number == "") 0 else number.toInt()
        }
        luggageRules[mainKey] = rules
    }

    return luggageRules
}

fun findProperBagsColorsForTransport(bagToPack: String): Int {
    var counter = 0
    luggageRules.forEach mainLoop@{bagRule ->
        if (bagRule.value.containsKey(bagToPack)) {
            validGoldCarryingBags.add(bagRule.key)
            counter++
            return@mainLoop
        }
        var tempCounter = 0
        bagRule.value.forEach {
            if (it.key == "no other") return@mainLoop
            val tempSet = recursiveBagSearch(bagToPack, it.key)
            if (tempSet > 0) tempCounter++
        }

        if (tempCounter > 0) counter++
    }

    return counter
}

fun recursiveBagSearch(bagToPack: String, bagToCheck: String): Int {
    var counter = 0
    luggageRules[bagToCheck]!!.forEach mainLoop@{
        if (validGoldCarryingBags.contains(it.key)) {
            counter++
            return@mainLoop
        }
        if (it.key == bagToPack) {
            validGoldCarryingBags.add(bagToCheck)
            counter++
            return@mainLoop
        }
        if (it.key == "no other") {
            return@mainLoop
        }
        counter += recursiveBagSearch(bagToPack, it.key)
    }
    return counter
}

fun main() {
    luggageRules = readLuggageData("data/data_day7")
    println(luggageRules.size)
    println(findProperBagsColorsForTransport("shiny gold"))
}
