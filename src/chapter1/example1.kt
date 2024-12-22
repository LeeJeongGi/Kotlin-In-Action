package chapter1

data class Person(
    val name: String,
    val age: Int? = null,
)

fun main() {
    val persons = listOf(Person("Lee"), Person("Kim", age = 29))

    val oldest = persons.maxBy{ it.age ?: 0}
    println("나이가 가장 많은 사람: $oldest")

}