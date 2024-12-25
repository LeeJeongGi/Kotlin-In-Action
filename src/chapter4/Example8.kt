package chapter4

class LengthCounter() {

    var counter: Int = 0
    private set

    fun addWord(word: String) {
        counter += word.length
    }
}

fun main() {
    val lengthCounter = LengthCounter()
    println(lengthCounter.counter)

    lengthCounter.addWord("Hello")
    println(lengthCounter.counter)
}
