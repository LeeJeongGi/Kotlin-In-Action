package chapter5

fun main() {
    listOf(1, 2, 3, 4).asSequence()
        .map { print("map($it) "); it * it }
        .filter { print("filter($it) "); it % 2 == 0 }
        .toList()

    val number = listOf(1, 2, 3, 4).map { it * it }.find { it > 3 }
    println(number)

}