package chapter6

fun sendEmailTo(email: String) {
    println("Send email to $email")
}

fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank()) {
        println("Input is empty")
    }
}

fun main() {
    var email: String? = "naver@com"
    email?.let {
        sendEmailTo(it)
    }

    email = null
    email?.let {
        sendEmailTo(it)
    }

    println(verifyUserInput("ttt"))
    println(verifyUserInput(""))
    println(verifyUserInput(null))

}