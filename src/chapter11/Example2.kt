package chapter11

class Greeter(
    val greeting: String
) {
    operator fun invoke(name: String) {
        println("$greeting, $name")
    }
}

fun main() {
    val bavarianGreeter = Greeter("bavarian")
    bavarianGreeter("Dmitry")
}