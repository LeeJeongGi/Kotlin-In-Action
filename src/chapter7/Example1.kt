package chapter7

data class Point(
    val x: Int,
    val y: Int,
)

operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}

operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}

operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}

operator fun Point.get(index: Int): Int {
    return when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

fun main() {
    val p1 = Point(10, 20)
    val p2 = Point(5, 50)
    println(p1 + p2)

    println(p1.times(1.5))

    val list = arrayListOf(1, 2)
    list += 3
    val newList = list + arrayListOf(3, 4)
    println(list)
    println(newList)

    val p4 = Point(10, 20)
    println(-p4)

    val p5 = Point(10, 20)
    println(p5[0])
}

