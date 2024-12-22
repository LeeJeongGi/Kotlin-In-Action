package chapter2

fun main(args: Array<String>) {
    println("Hello, world!!")

    println(max1(1,2))

    val question = "이런 식으로 선언한다."
    val answer = 42
    val answer2: Int = 42

}

/**
 * 블록이 본문인 함수
 */
fun max1(a: Int, b: Int): Int {
    return if (a > b) a else b
}

/**
 * 식이 본문인 함수
 */
fun max2(a: Int, b: Int): Int = if (a > b) a else b

