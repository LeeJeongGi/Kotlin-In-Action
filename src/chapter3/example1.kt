package chapter3

fun main() {
    val set = hashSetOf(1, 7, 53)
    val list = arrayListOf(1, 7, 53)
    val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")

    println(set.javaClass)
    println(list.javaClass)
    println(map.javaClass)

    val strings = listOf("first", "second", "third")
    println(strings.last())

    val numbers = setOf(1, 2, 3, 4, 5)
    println(numbers.max())

    val list2 = listOf(1, 2, 3, 4, 5)
    println(list2)
}