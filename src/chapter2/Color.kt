package chapter2

enum class Color(
    var r: Int,
    var g: Int,
    var b: Int,
) {
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(1,2,3),
    YELLOW(4,5,6);

    fun rgb(): Int {
        return (r * 256 + g) * 256 + b
    }
}



fun main() {
    println(Color.BLUE.rgb())
    println(getMnemonic(Color.RED))
}

fun getMnemonic(color: Color) {
    when (color) {
        Color.RED -> "Richard"
        Color.GREEN -> "Gold"
        Color.BLUE -> "Blue"
        Color.YELLOW -> "Yellow"
    }
}

fun getWarmth(color: Color) = when (color) {
    Color.RED, Color.GREEN -> "worm"
    Color.BLUE, Color.YELLOW -> "cold"
}

fun mix(c1: Color, c2: Color) = when(setOf(c1, c2)) {
    setOf(Color.RED, Color.YELLOW) -> Color.RED
    setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
    setOf(Color.BLUE, Color.RED, Color.YELLOW) -> Color.BLUE
    else -> {
        throw Exception("Dirty color")
    }
}

fun mixOptimized(c1: Color, c2: Color) = when {
    (c1 == Color.RED && c2 == Color.YELLOW) || (c1 == Color.BLUE && c2 == Color.YELLOW) -> {
        Color.RED
    }
    else -> {
        throw Exception("Dirty color")
    }
}
