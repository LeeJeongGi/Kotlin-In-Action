package chapter3

fun main(args: Array<String>) {
    val list = listOf("args: ", *args)
    println(list)

    val maps = mapOf(1 to "one", 2 to "two", 3 to "three")

    /**
        1.to("one")
        1 to "one"

        같은 의미
    */

    val (number, name) = 1 to "one"

    println("12.345-6.A".split("[.\\-]".toRegex()))
    println("12.345-6.A".split(".", "-"))
}