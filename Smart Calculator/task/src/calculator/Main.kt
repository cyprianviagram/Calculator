package calculator

import kotlin.system.exitProcess

enum class RegexValidation(val regex: Regex) {
    IS_ASSIGNMENT("[A-Za-z]+(\\w*(\\s*=*(\\s*(-?\\w+)*)))*".toRegex()),
    IS_COMMAND("/.+".toRegex()),
    VALID_EXPRESSION("(([+]|-)?(\\d+)|[A-Za-z]+)+(\\s+([+]+|-+)\\s+-?((\\d+)|[A-Za-z]+))*".toRegex()),
}

fun main() {
    calculator()
}

fun calculator() {
    val mapOfVariables = mutableMapOf<String, Long>()
    while(true) {
        val userInput = readln()
        when {
            userInput.trim().matches(RegexValidation.IS_ASSIGNMENT.regex) -> assignmentProcessor(userInput, mapOfVariables)
            userInput.trim().matches(RegexValidation.VALID_EXPRESSION.regex) -> sumOrSubtraction(userInput)
            userInput.trim().matches(RegexValidation.IS_COMMAND.regex) -> commandProcessor(userInput)
            userInput.trim() == "" -> calculator()
            else -> {
                println("Invalid expression")
            }
        }
    }
}

fun assignmentProcessor(input: String, map: MutableMap<String, Long>) {
    val leftPartOfAssignment = input.substringBefore("=").trim()
    val rightPartOfAssignment = input.substringAfter("=", "").trim()
    when {
        leftPartOfAssignment.contains("\\d".toRegex()) -> {
            println("Invalid identifier")
        }
        rightPartOfAssignment.contains("=".toRegex()) || rightPartOfAssignment.contains("\\d+[A-Za-z]+".toRegex()) -> {
            println("Invalid assignment")
        }
        else -> assignVariable(leftPartOfAssignment, rightPartOfAssignment, map)
    }
}

fun assignVariable (leftPart: String, rightPart: String, map: MutableMap<String, Long>) {
   try {
       when {
           map.contains(leftPart) && rightPart == "" -> println(map[leftPart])
           rightPart.contains("\\D".toRegex()) -> map[leftPart] = map[rightPart]!!
           else -> map[leftPart] = rightPart.toLong()
        }
   } catch (e: Exception) {
       println("Unknown variable")
   }
}
fun commandProcessor(input: String) {
    when (input) {
        "/help" -> help()
        "/exit" -> exit()
        else -> {
            println("Unknown command")
        }
    }
}

fun sumOrSubtraction(input: String) {
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
}