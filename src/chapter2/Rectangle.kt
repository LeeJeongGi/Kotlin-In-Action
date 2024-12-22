package chapter2

import java.util.*

class Rectangle(
    val height: Int,
    val width: Int,
) {
    val isSquare: Boolean
        get() {
            return height == width
        }
}

fun createRandomRectangle(): Rectangle {
    val random = Random()
    return Rectangle(random.nextInt(), random.nextInt())
}

fun main() {
    val rectangle = Rectangle(5, 5)
    println(rectangle.isSquare)
}