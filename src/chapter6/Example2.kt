package chapter6

class Person(
    val firstName: String,
    val lastname: String,
) {
    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other !is Person) return false

        val otherPerson = other as? Person ?: return false

        return otherPerson.firstName == firstName && otherPerson.lastname == lastname
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastname.hashCode()
        return result
    }
}

fun main() {
    val p1 = Person("Alice", "Smith")
    val p2 = Person("Alice", "Smith")
    println(p1 == p2)
    println(p1.equals(null))
}