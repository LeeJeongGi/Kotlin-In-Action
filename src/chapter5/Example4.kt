package chapter5

class Book(
    val title: String,
    val authors: List<String>,
)

fun main() {
    val strings = listOf("abc", "def")
    println(strings.flatMap { it.toList() })

    val books = listOf(
        Book("Thursday Next", listOf("Jasper Fforde")),
        Book("Mort", listOf("Terry Pratchett")),
        Book("Good Morning", listOf("Terry Pratchett", "Neil GaiMan")),
    )
    println(books.flatMap { it.authors }.toSet())
}

