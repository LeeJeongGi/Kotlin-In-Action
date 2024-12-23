package chapter3

fun View.showOf() = println("I'm a view")
fun Button.showOf() = println("I'm a button")

fun main() {
    val view: View = Button()
    view.showOf()
}