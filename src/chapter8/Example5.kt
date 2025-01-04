package chapter8

data class Person2(
    val name: String,
    val age: Int,
)

fun lookForAlice(persons: List<Person2>) {
    for (person in persons) {
        if (person.name == "Alice") {
            println("Found!!")
            return
        }
    }
    println("Alice is not found")
}

fun main() {
    val persons = listOf(Person2("Alice", 25), Person2("dudu", 25))
    lookForAlice(persons)
}