package calculator

import kotlin.system.exitProcess

fun main() {
    calculator()
}

fun calculator() {
    when (val userInput = readln()){
        "" -> calculator()
        "/exit" -> exit()
        "/help" -> help()
        else -> sumOrSubtractionOfIntegers(userInput)
    }
}

fun sumOrSubtractionOfIntegers(input: String) {
    val listOfIntegers: List<Long> = input.split("\\s+[+]+\\s+|\\s+-+\\s+".toRegex()).map {it.toLong()}.toList()
    val listOfOperators = "[+]+|-+".toRegex().findAll(input).map { it.value }.toList()
    var result = listOfIntegers[0]
    val droppedListOfIntegers = listOfIntegers.drop(1)
    droppedListOfIntegers.forEach loop@{i ->
        val indexOfI = droppedListOfIntegers.indexOf(i)
        if (listOfOperators[indexOfI].contains("+") || listOfOperators[indexOfI].contains("--")) {
            result += i
        } else result -= i
    }
    println(result)
    calculator()
}


fun exit() {
    println("Bye!")
    exitProcess(0)
}

fun help() {
    println("The program calculates the sum of numbers")
    calculator()
}