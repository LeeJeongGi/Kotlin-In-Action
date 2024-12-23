package chapter3

fun parsePath(path: String) {
    val directory = path.substringBeforeLast("/")
    val fullName = path.substringAfterLast("/")

    val fileName = fullName.substringBeforeLast(".")
    val extension = fullName.substringAfterLast(".")

    println("$directory/$fileName.$extension")
}

fun main() {
    parsePath("/tmp/path/to/file.txt")
}