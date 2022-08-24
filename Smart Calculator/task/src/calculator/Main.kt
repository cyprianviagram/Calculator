package calculator

fun main() {
    sumOfTwoIntegers()
}

fun sumOfTwoIntegers() {
    val userInput = readln()
    when {
        userInput.isEmpty() -> sumOfTwoIntegers()
        !userInput.contains(' ') -> {
            try {
                println(userInput.toInt())
                sumOfTwoIntegers()
            } catch (e: Exception) {
                if (userInput == "/exit") {
                    println("Bye!")
                    return
                } else sumOfTwoIntegers()
            }
        } else -> {
            val (firstInteger, secondInteger) = userInput.split(' ').map {it.toLong()}
        println(firstInteger + secondInteger)
        sumOfTwoIntegers()
        }
    }
}
