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
    try {
        input.trim().toLong()
        println(input.trim())
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
        Enter two or more numbers with either plus operator (" + ") or minus operator (" - ") between the numbers with spaces before and after the operator.
            Example: "3 + 5 - 4 + 1"
            Two or any even number of minus operators will result in addition ( " -- " equals " + " ). An odd number of minus operators will result in subtraction.
            Example: "2 -- 2" is equal to "2 + 2" and result will be "4"
            For negative numbers do not put space after minus sign.
            Example: "-2 + -5"
            Enter /help to display these instructions.
            Enter /exit to quit program.
        """.trimIndent())
    calculator()
}