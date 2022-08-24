package calculator

import kotlin.system.exitProcess

fun main() {
    sumOfIntegers()
}

fun sumOfIntegers() {
    val userInput = readln()
    when {
        userInput.isEmpty() -> sumOfIntegers()
        !userInput.contains(' ') -> printOnlyIntegerInfoOrTerminate(userInput)
        else -> {
            val (firstInteger, secondInteger) = userInput.split(' ').map {it.toLong()}
        println(firstInteger + secondInteger)
        sumOfIntegers()
        }
    }
}

fun printOnlyIntegerInfoOrTerminate(input: String) {
    try {
        println(input.toInt())
        sumOfIntegers()
    } catch (e: Exception) {
        when (input) {
            "/exit" -> exit()
            "/help" -> help()
            else -> sumOfIntegers()
        }
    }
}

fun exit() {
    println("Bye!")
    exitProcess(0)
}

fun help() {
    println("The program calculates the sum of numbers")
    sumOfIntegers()
}
