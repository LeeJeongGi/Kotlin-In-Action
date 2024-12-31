package chapter6

fun <T> copyElements(
    source: Collection<T>,
    target: MutableCollection<T>,
) {
    for (item in source) {
        target.add(item)
    }
}

fun main() {
    val source = arrayListOf(1, 2, 3)
    val target = arrayListOf(4)

    copyElements(source, target)
    println(target)
}