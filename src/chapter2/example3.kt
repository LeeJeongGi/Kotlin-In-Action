package chapter2

fun main(args: Array<String>) {
    val name = if (args.size > 0) args[0] else null
    println("Hello, $name!")
}