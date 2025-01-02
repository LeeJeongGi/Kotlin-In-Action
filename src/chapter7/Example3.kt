package chapter7

data class Rectangle(
    val upperLeft: Point,
    val lowerRight: Point
)

operator fun Rectangle.contains(p: Point): Boolean {
    return p.x in upperLeft.x until lowerRight.x && p.y in upperLeft.y until lowerRight.y
}

fun main() {
    val rectangle = Rectangle(Point(10, 20), Point(50, 50))
    println(rectangle.contains(Point(20, 30)))
    println(rectangle.contains(Point(5, 5)))

    val point = Point(5, 5)
    val (x, y) = point
    println(x)
    println(y)
}