package chapter5

data class Person(
    val name: String,
    val age: Int,
)

fun main() {
    val persons = listOf(Person("Alice", 29), Person("Bob", 31), Person("Carol", 33))
    println(persons.maxBy { it.age })

    println(persons.maxBy{ p: Person -> p.age})
    println(persons.maxBy(Person::age))

    val name = persons.joinToString(separator = " ", transform = { p: Person -> p.name})
    println(name)

    val sum = { x: Int, y: Int -> x + y }
    println(sum(1, 2))

    val sum2 = { x: Int, y: Int ->
        println("Computing the sum of $x and $y...")
        x + y
    }
    println(sum2(1, 2))

    val canBeInClubPerson = persons.find { it.age <= 31}
    println(canBeInClubPerson)

    val canBeInClubAllPerson = persons.all { it.age <= 31}
    println(canBeInClubPerson)

    val persons2 = listOf(Person("Alice", 29), Person("Bob", 31), Person("Carol", 31))
    val temp = persons2.groupBy { it.age }

    println(temp.javaClass)
    println(temp)
}
