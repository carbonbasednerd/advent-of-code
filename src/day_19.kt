import java.io.File

open class BaseRule() {
}

data class EndRule(val value: Char) : BaseRule()
data class BranchRule(val ands: List<List<Int>>, val loops: Boolean) : BaseRule()

val rulesData = mutableMapOf<Int, BaseRule>()
val imageData = mutableListOf<String>()

fun readRulesAndData(fileName: String) {
    var parsingRules = true
    File(fileName).forEachLine parse@{
        if (it == "") {
            parsingRules = false
            return@parse
        }
        if (parsingRules) {
            val tempString = it.split(":")
            val ruleNumber = tempString.first().toInt()
            if (tempString[1].contains("a")) {
                rulesData[ruleNumber] = EndRule('a')
            } else if (tempString[1].contains("b")) {
                rulesData[ruleNumber] = EndRule('b')
            } else {
                if (tempString[1].contains("|")) {
                    val orSplit = tempString[1].split("|")
                    val andList = mutableListOf<List<Int>>()
                    var loopy = false
                    orSplit.forEach { s ->
                        val numbers = s.split(" ")
                        var branchList = mutableListOf<Int>()
                        numbers.forEach { t ->
                            if (t.isNotEmpty()) {
                                if (t.toInt() == ruleNumber) {
                                    loopy = true
                                } else {
                                    branchList.add(t.toInt())
                                }

                            }
                        }
                        andList.add(branchList)
                    }
                    rulesData[ruleNumber] = BranchRule(andList, loopy)
                } else {
                    val numbers = tempString[1].split(" ")
                    val branchList = mutableListOf<Int>()
                    var loopy = false
                    numbers.forEach { s ->
                        if (s.isNotEmpty()) {
                            branchList.add(s.toInt())
                            if (s.toInt() == ruleNumber) loopy = true
                        }
                    }
                    rulesData[ruleNumber] = BranchRule(mutableListOf(branchList), loopy)
                }
            }
        } else {
            imageData.add(it)
        }
    }
}

lateinit var possibleCombinations: List<String>

// part one
val ruleMap = mutableMapOf<Int, List<String>>()
var depth = 0
fun buildRules(ruleNumber: Int): List<String> {
    println("Depth ${++depth}")
    if (ruleMap.containsKey(ruleNumber)) {
        return ruleMap[ruleNumber]!!
    } else {
        if (rulesData[ruleNumber] is EndRule) {
            val tempList = listOf((rulesData[ruleNumber] as EndRule).value.toString())
            ruleMap[ruleNumber] = tempList
            return tempList
        } else {
            val branches = rulesData[ruleNumber] as BranchRule
            val listOfAnds = mutableListOf<List<String>>()
            branches.ands.forEach {
                var possibleStrings = mutableListOf<String>()
                it.forEach { i ->
                    val returnedLists = buildRules(i)
                    val newList = mutableListOf<String>()
                    if (possibleStrings.isNotEmpty()) {
                        possibleStrings.forEach { x ->
                            returnedLists.forEach { y ->
                                newList.add(x.plus(y))
                            }
                        }
                    } else {
                        newList.addAll(returnedLists)
                    }
                    possibleStrings = newList
                }
                listOfAnds.add(possibleStrings)
            }
            val flattenedList = listOfAnds.flatten()
            ruleMap[ruleNumber] = flattenedList
            return flattenedList
        }
    }
}

fun solvePart1(): Int {
    var counter = 0
    imageData.forEach image@{ data ->
        possibleCombinations.forEach inner@{
            //match up to S
            val startIndex = it.indexOf('S')

            if (startIndex == -1) {
                if (data.length != it.length) return@inner
                if (data.matches("^${it}$".toRegex())) {
                    counter++
                    return@image
                }
            } else {
                val endIndex = it.indexOf('E')
                var repeatingString = it.substring(startIndex + 1, endIndex)
                var dataString = data.substring(0, startIndex)
                var itSub = it.substring(0, startIndex)
                if (dataString.matches("^${itSub}$".toRegex())) {
                    var searching = true
                    var index = startIndex
                    val subSize = repeatingString.length
                    var matches = 0
                    while (searching) {
                        if (startIndex < (data.length) && subSize < (data.length)) {
                            dataString = data.substring(startIndex, (startIndex + subSize - 1))
                            if (dataString.matches("^${repeatingString}$".toRegex())) {
                                matches++
                            } else {
                                searching = false
                            }
                            index += subSize
                        } else {
                            searching = false
                        }

                    }
                    if (matches > 0) {
                        index += subSize
                        itSub = it.substring(endIndex + 1)
                        dataString = data.substring(endIndex)
                        if (dataString.matches("^${itSub}$".toRegex())) {
                            counter++
                            return@image
                        }
                    }
                }
            }

        }

    }
    return counter
}

//part 2
//this has some logic from part two - initially tried to make it all work as one
//got frustrated and made a second function
fun solvePart2(): Int {
    var counter = 0
    imageData.forEach image@{ image ->
        var keepLooking = true
        var index = 0
        var fourtyTwoCount = 0
        while (keepLooking) {
            var newIndex = index
            try {
                for (matchString in ruleMap[42]!!) {
                    val length = matchString.length
                    if (image.substring(index, length+index).matches("^${matchString}$".toRegex())) {
                        newIndex += length
                        fourtyTwoCount++
                        break
                    }
                }
            } catch (error: StringIndexOutOfBoundsException) {
                keepLooking = false
            }
            if (index == newIndex) {
                keepLooking = false
            } else {
                index = newIndex
            }
        }
        keepLooking = true

        if (index >= image.length || index == 0) {
            return@image
        }

        var thirtyOneCount = 0
        while (keepLooking) {
            var newIndex = index
            try { //haha so dumb
                for (matchString in ruleMap[31]!!) {
                    val length = matchString.length
                    if (image.substring(index, length+index).matches("^${matchString}$".toRegex())) {
                        newIndex += length
                        thirtyOneCount++
                        break
                    }
                }
            } catch (error: StringIndexOutOfBoundsException) {
                keepLooking = false
            }
            if (index == newIndex) {
                keepLooking = false
            } else {
                index = newIndex
            }
        }
        val imageLength = image.length
        if (index == imageLength && fourtyTwoCount > thirtyOneCount) {
            counter++
            println("$image 31count = $thirtyOneCount 42count = $fourtyTwoCount")
        }
    }
    return counter
}

fun main() {
    readRulesAndData("data/test_data_day19_3")
    println(rulesData)
    println(imageData)
    println("building rules")
    possibleCombinations = buildRules(0)
    println("Possible combinations ${possibleCombinations.size}")
    println("Found ${solvePart1()} matches for Rule 0")
    println("Found ${solvePart2()} matches for Rule 0 part 2")
}