package calculator

import kotlin.system.exitProcess

fun main() {
    sumOfIntegersModulus()
}

fun sumOfIntegersModulus() {
    when (val userInput = readln()){
        "" -> sumOfIntegersModulus()
        "/exit" -> exit()
        "/help" -> help()
        else -> sumOfIntegers(userInput)
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
    val listOfIntegers = input.split(" ").map {it.toLong()}.toList()
    println(listOfIntegers.sum())
    sumOfIntegersModulus()
}
