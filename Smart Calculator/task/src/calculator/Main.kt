package calculator
import java.util.Stack
import kotlin.math.pow
import kotlin.system.exitProcess

enum class RegexValidation(val regex: Regex) {
    IS_ASSIGNMENT("[A-Za-z]+(\\w*(\\s*=*(\\s*(-?\\w+)*)))*".toRegex()),
    IS_COMMAND("/.+".toRegex()),
    IS_EXPRESSION("(([(])*([+]|-)?(\\d+)|[A-Za-z]+)+(\\s+([+]+|-+|\\*|/|\\^)\\s+([(])*-?((\\d+)|[A-Za-z]+)([)])*)*".toRegex()),
}

val operatorsValues = mapOf<String, Int> (
    "+" to 1,
    "-" to 1,
    "*" to 2,
    "/" to 2,
    "^" to 3
        )

fun main() {
    calculator()
}

fun calculator() {
    val mapOfVariables = mutableMapOf<String, Long>()
    while(true) {
        val userInput = readln().trim()
        when {
            userInput.matches(RegexValidation.IS_ASSIGNMENT.regex) -> assignmentProcessor(userInput, mapOfVariables)
            userInput.matches(RegexValidation.IS_EXPRESSION.regex) -> expressionProcessor(userInput, mapOfVariables)
            userInput.matches(RegexValidation.IS_COMMAND.regex) -> commandProcessor(userInput)
            userInput == "" -> calculator()
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
           rightPart.contains("[^-]\\D".toRegex()) -> map[leftPart] = map[rightPart]!!
           else -> {
               map[leftPart] = rightPart.toLong()
           }
       }
   } catch (e: Exception) {
       println("Unknown variable")
   }
}
fun commandProcessor(input: String) {
    when (input.lowercase()) {
        "/help" -> help()
        "/exit" -> exit()
        else -> {
            println("Unknown command")
        }
    }
}

fun expressionProcessor(input: String, map: MutableMap<String, Long>) {
    val input = try {
        replaceVariablesToIntegers(input, map)
    } catch (e: Exception) {
        println("Invalid expression")
        return
    }
    if (parenthesesValidator(input)) {
        try {
            println(input.toLong())
        } catch (e: Exception) {
            val postfixExpression = infixToPostfix(inputProcessor(input).split(" ").map { it }.toList())
            println(postfixToResult(postfixExpression))
        }
    } else println("Invalid expression")
}

fun infixToPostfix (infixList: List<String>): List<String> {
    val stack = Stack<String>()
    val postfixExpression = mutableListOf<String>()
    infixList.forEach {
        when {
            it.contains("\\d".toRegex()) -> postfixExpression.add(it)
            it == "(" -> stack.add(it)
            it == ")" -> {
                while (stack.last() != "(") {
                    postfixExpression.add(stack.last())
                    stack.pop()
                }
                stack.pop()
            }
            stack.isEmpty() || stack.last() == "(" -> stack.push(it)
            operatorsValues[it]!! > operatorsValues[stack.last()]!! -> stack.push(it)
            operatorsValues[it]!! <= operatorsValues[stack.last()]!! -> {
                while (stack.isNotEmpty() && (operatorsValues[it]!! <= operatorsValues[stack.last()]!! || stack.last() != "(" )) {
                    postfixExpression.add(stack.last())
                    stack.pop()
                }
                stack.push(it)
            }
        }
    }
    while (stack.isNotEmpty()) {
        postfixExpression.add(stack.last())
        stack.pop()
    }
    return postfixExpression.toList()
}

fun postfixToResult(postfixList: List<String>): String {
    val stack = Stack<String>()
    var result: String
    postfixList.forEach {
        when {
            it.contains("\\d+".toRegex()) -> stack.push(it)
            else -> {
                result = operationProcessor(stack, it)
                stack.pop()
                stack.push(result)
            }
        }
    }
    return stack.pop()
}

fun operationProcessor (stack: Stack<String>, operator: String): String {
    val b = stack.last().toDouble()
    stack.pop()
    val a = stack.last().toDouble()
    return when (operator) {
        "+" -> (a + b).toString()
        "-" -> (a - b).toString()
        "/" -> (a / b).toString()
        "*" -> (a * b).toString()
        "^" -> a.pow(b).toString()
        else -> null.toString()
    }
}

fun parenthesesValidator (input: String): Boolean {
    val onlyParentheses = input.replace("[^()]".toRegex(), "")
    val numberOfLeftParenthesis = onlyParentheses.count { it == '(' }
    val numberOfRightParenthesis = onlyParentheses.count { it == ')'}
    return numberOfLeftParenthesis == numberOfRightParenthesis
}

fun replaceVariablesToIntegers (input: String, map: MutableMap<String, Long>): String {
    var input = input
    return if (input.contains("[A-Za-z]+".toRegex())) {
        val variables = "[A-Za-z]+".toRegex().findAll(input)
        val listOfVariables = variables.map {it.value}.toList()
        // println(listOfVariables)
        listOfVariables.forEach {
            input = input.replace(it, map[it]!!.toString())
        }
        return input
    } else {
        input
    }
}

fun inputProcessor (input: String): String {
    return input.replace(
        "\\(".toRegex(), "( "
    ).replace(
        "\\)".toRegex(), " )"
    ).replace(
        "--".toRegex(), "+"
    ).replace(
        "[+]+".toRegex(), "+"
    ).replace(
        "[+]-+".toRegex(), "-")
}

fun help() {
    println("""
        The program also can store variables.
        Enter a name of variable (only latin letters) plus operator (" = ") plus an integer or another previously declared variable.
            Valid declarations: a = 1, b = 2, m=3, big = 3, BIG = 33, a = b (if previously declared)
            Invalid declarations: a1 = 3, a = a2, a = b = c, a = 8 = c
        The program calculates a sum and a subtraction of numbers or variables. 
        Enter two or more numbers or variables with plus operator (" + ") or minus operator (" - ") between the numbers or variables with spaces before and after the operator.
            Any even number of minus operators results in addition. Any odd number of minus operators results in subtraction
            For negative numbers do not put space after minus operator.
        Enter /help to display these instructions.
        Enter /exit to shut down the program.
        """.trimIndent())
}

fun exit() {
    println("Bye!")
    exitProcess(0)
}

