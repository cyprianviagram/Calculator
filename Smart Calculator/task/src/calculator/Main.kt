package calculator

import kotlin.system.exitProcess

fun main() {
    sumOfIntegersModulus()
}

fun sumOfIntegersModulus() {
    val userInput = readln()
    when {
        userInput.isEmpty() -> sumOfIntegersModulus()
        !userInput.contains(' ') -> printOnlyIntegerInfoOrTerminate(userInput)
        else -> sumOfIntegers(userInput)
    }
}

fun printOnlyIntegerInfoOrTerminate(input: String) {
    try {
        println(input.toLong())
        sumOfIntegersModulus()
    } catch (e: Exception) {
        when (input) {
            "/exit" -> exit()
            "/help" -> help()
            else -> sumOfIntegersModulus()
        }
    }
}

fun exit() {
    println("Bye!")
    exitProcess(0)
}

fun help() {
    println("The program calculates the sum of numbers")
    sumOfIntegersModulus()
}

fun sumOfIntegers(input: String) {
    val listOfIntegers = input.split(' ').map {it.toLong()}.toList()
    println(listOfIntegers.sum())
    sumOfIntegersModulus()
}
