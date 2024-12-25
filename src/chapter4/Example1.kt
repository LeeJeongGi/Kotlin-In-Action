package chapter4

interface Clickable {
    fun click()

    fun showOff() = println("I'm clickable!")
}

interface Focusable {
    fun setFocus(b: Boolean) = println("I ${if (b) "got" else "lost"} focus.")

    fun showOff() = println("I'm focused!")
}

class Button : Clickable, Focusable {
    override fun click() {
        println("I'm clickable!")
    }

    override fun showOff() {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }

}

fun main() {
    val button = Button()
    button.showOff()
    button.setFocus(true)
    button.click()
}