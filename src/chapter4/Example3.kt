package chapter4

internal open class TalkActiveButton {
    private fun yell() = println("I'm sorry!")
    protected fun whisper() = println("Let's talk!")
}

internal fun TalkActiveButton.giveSpeech() {

}