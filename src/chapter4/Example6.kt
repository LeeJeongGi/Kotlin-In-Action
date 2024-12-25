package chapter4

interface User {
    val nickName: String
}

class PrivateUser(override val nickName: String) : User

class SubscribedUser(val email: String) : User {
    override val nickName: String
        get() = email.substringBefore('@')
}

class FacebookUser(val accountId: Int) : User {
    override val nickName = "Test"
}

fun main() {
    println(PrivateUser("Lee").nickName)
    println(SubscribedUser("apple4hk@naver.com").nickName)
    println(FacebookUser(3).nickName)
}

