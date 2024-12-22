package chapter2

import java.util.*

val binaryReps = TreeMap<Char, String>()

fun main() {
    for (c in 'A'..'Z') {
        val binary = Integer.toBinaryString(c.toInt())
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    val list = listOf("10", "11", "12")
    for ((index, element) in list.withIndex()) {
        println("$index: $element")
    }

    println(isLetter('d'))
    println(isDigit('3'))
    println(recognize(c = 'e'))
    println(recognize(c = '3'))
}

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isDigit(c: Char) = c in '0'..'9'

fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "it's a digit!!!!"
    in 'a'..'z' -> "it's a letter!!!!"
    else -> "i don't know"
}