package chapter5

fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7)
    println(numbers.filter { it % 2 == 0})
    println(numbers.map { it * it})
}