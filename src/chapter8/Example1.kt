package chapter8

fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("result: $result")
}

fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in 0 until length) {
        val element = get(index)
        if (predicate(element)) {
            sb.append(element)
        }
    }
    return sb.toString()
}

fun main() {
    val sum = {x: Int, y: Int -> x + y }
    println(sum(1, 2))

    println(twoAndThree(sum))

    println("abc".filter { it in 'a'..'z' })
}