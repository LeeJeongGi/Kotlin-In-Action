package chapter6

fun strLen(s: String?): Int? {
    return s?.length
}

fun main() {
    println(strLen("test"))
    println(strLen(null))
}

class Employee(
    val name: String,
    val manager: Employee?,
)

fun managerName(employee: Employee): String? {
    return employee.manager?.name
}

fun foo(s: String?) {
    val t: String = s ?: ""
}

fun strLenSafe(s: String?): Int = s?.length ?: 0