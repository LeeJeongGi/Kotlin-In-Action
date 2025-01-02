package chapter7

data class NameComponents(
    val name: String,
    val extension: String,
)

fun splitFilename(filename: String): NameComponents {
    val result = filename.split('.', limit = 2)
    return NameComponents(result[0], result[1])
}

fun printEntries (map: Map<String, String>) {
    for ((key, value) in map) {
        println("$key: $value")
    }
}

fun main() {
    val (name, ext) = splitFilename("test.txt")
    println(name)
    println(ext)

    val map = mapOf("key1" to "value1", "key2" to "value2")
    println(printEntries(map))
}