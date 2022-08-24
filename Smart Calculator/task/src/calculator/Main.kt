package calculator

fun main() {
    val (firstInt, secondInt) = readln().split(' ').map { it.toInt() }
    println(firstInt + secondInt)
}
