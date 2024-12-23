package chapter3

fun <T> Collection<T>.joinToString(
    collection: Collection<T>,
    separator: String = ",",
    prefix: String = "",
    postfix: String = "",
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }

        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}

fun Collection<String>.join(
    separator: String = ",",
    prefix: String = "",
    postfix: String = "",
) = joinToString(separator, prefix, postfix)

fun main() {
    val list = listOf(1, 2, 3, 4, 5)
    println(list.joinToString(list, separator = "; ", prefix = "(", postfix = ")"))

    println(list.joinToString(list))
    println(list.joinToString(list, "^"))

    println("Kotlin".lastChar())
}