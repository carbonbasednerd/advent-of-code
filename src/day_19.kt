import java.io.File

open class BaseRule() {
}
data class EndRule(val value: Char): BaseRule()
data class BranchRule(val ands: List<List<Int>>): BaseRule()

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
                    orSplit.forEach {s ->
                        val numbers = s.split(" ")
                        val branchList = mutableListOf<Int>()
                        numbers.forEach {t->
                            if (t.isNotEmpty()) branchList.add(t.toInt())
                        }
                        andList.add(branchList)
                    }
                    rulesData[ruleNumber] = BranchRule(andList)
                } else {
                    val numbers = tempString[1].split(" ")
                    val branchList = mutableListOf<Int>()
                    numbers.forEach {s->
                        if (s.isNotEmpty()) branchList.add(s.toInt())
                    }
                    rulesData[ruleNumber] = BranchRule(mutableListOf(branchList))
                }
            }
        } else {
            imageData.add(it)
        }
    }
}
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

fun main() {
    readRulesAndData("data/data_day19")
    println(rulesData)
    println(imageData)
    println("building rules")
    val possibleCombinations = buildRules(0)
    var counter = 0
    println("rules built")
    imageData.forEach image@{data ->
        println("checking image ${data}")
        possibleCombinations.forEach {
            if (data.length != it.length) return@image
            if (data.matches("^${it}$".toRegex())) {
                counter++
                return@image
            }
        }

    }
    println("Found $counter matches for Rule 0")
}