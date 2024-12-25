package chapter4

class User3(val name: String) {

    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was changed for $name:
                "$field" -> "$value".
            """.trimIndent())
            field = value
        }
}

fun main() {
    val user = User3("Lee")
    user.address = "dkfjsdlkfjsdlkjfsdlk;jfdkl 323, 23123"
    println(user.address)
}
