package calculator

import kotlin.system.exitProcess

fun main() {
    calculator()
}

fun calculator() {
    val invalidExpression = "(\\s*([+]|-)?\\d+)+(\\s+([+]+|-+)\\s+-?\\d+)*".toRegex()
    val userInput = readln()
    when {
        userInput == "" -> calculator()
        userInput == "/exit" -> exit()
        userInput == "/help" -> help()
        !userInput.matches(invalidExpression) -> {
            if (userInput[0] == '/') println("Unknown command") else println("Invalid expression")
            calculator()
        }
        else -> sumOrSubtractionOfIntegers(userInput)
    }
}

fun sumOrSubtractionOfIntegers(input: String) {
    try {
        println(input.trim().toLong())
    } catch (e: Exception) {
        var result: Long = if (input.substringBefore(" +").length < input.substringBefore(" -").length) {
            input.substringBefore(" +").trim().toLong()
        } else input.substringBefore(" -").trim().toLong()

        val processedInput = inputProcessor(input).replace(result.toString(), "")

        val listOfOperators = "[+]|-".toRegex().findAll(processedInput).map { it.value }.toList()
        val processedInputWithoutOperator = processedInput.replace("[+]|-".toRegex(), " ").trim()
        val listOfIntegers: List<Long> = processedInputWithoutOperator.split(" ").map { it.toLong() }.toList()

        var counter = 0
        listOfIntegers.forEach {
            if (listOfOperators[counter] == "+") result += it else result -= it
            counter++
        }
        println(result)
    }
    calculator()
}

fun inputProcessor (input: String): String {
    return input.replace(
        "\\s+".toRegex(), ""
    ).replace(
        "--".toRegex(), "+"
    ).replace(
        "[+]+".toRegex(), "+"
    ).replace(
        "[+]-+".toRegex(), "-")
}

fun exit() {
    println("Bye!")
    exitProcess(0)
}

fun help() {
    println("""
        The program calculates a sum and a subtraction of numbers. 
        Enter two or more numbers with plus operator (" + ") or minus operator (" - ") between the numbers with spaces before and after the operator.
            Any even number of minus operators results in addition. Any odd number of minus operators results in subtraction
            For negative numbers do not put space after minus operator.
            Enter /help to display these instructions.
            Enter /exit to quit program.
        """.trimIndent())
    calculator()
}