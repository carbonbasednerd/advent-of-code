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

fun buildRules(ruleNumber: Int) {
    // loop through and build all possible strings?
}

//read in data - break into classes for the pattern matching logic
//read in test data as strings
//build all possible patterns for a particular entry, save
//loop through data and match exacts
fun main() {
    readRulesAndData("data/test_data_day19")
    println(rulesData)
    println(imageData)
}