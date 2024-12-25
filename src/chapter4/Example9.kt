package chapter4

class Client(val name: String, val postalCode: Int) {
    override fun toString(): String {
        return "Client(name='$name', postalCode=$postalCode)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other !is Client) return false

        if (postalCode != other.postalCode) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = postalCode
        result = 31 * result + name.hashCode()
        return result
    }
}

data class Client2(val name: String, val postalCode: Int)

fun main() {
    val client = Client("Lee", postalCode = 321)
    println(client)
}