package chapter5

fun salute() = println("salute")

fun printMessageWithPrefix(message: Collection<String>, prefix: String) {
    message.forEach {
        println("$prefix: $it")
    }
}

fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0

    responses.forEach { a ->
        if (a.startsWith("4")) {
            clientErrors++
        } else if (a.startsWith("5")) {
            serverErrors++
        }
    }

    println("$clientErrors client errors, $serverErrors server errors")
}

fun main() {
    val msg = listOf("403 Forbidden", "404 Not Found", "500 Server Error")
    println(printMessageWithPrefix(msg, prefix = "Error :"))

    printProblemCounts(msg)

    run(::salute)
}