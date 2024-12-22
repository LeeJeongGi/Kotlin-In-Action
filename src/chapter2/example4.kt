package chapter2

interface Expr

class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval(e: Expr): Int {
    if (e is Num) {
        val n = e as Num
        return n.value
    }

    if (e is Sum) {
        return eval(e.right) + eval(e.left)
    }

    throw IllegalArgumentException("Unknown expression")
}

fun eval_v2(e: Expr): Int =
    if (e is Num) {
        e.value
    }else if (e is Sum) {
        eval_v2(e.right) + eval_v2(e.left)
    } else {
        throw IllegalArgumentException("Unknown expression")
    }

fun eval_v3(e: Expr): Int =
    when (e) {
        is Num -> {
            e.value
        }

        is Sum -> {
            eval_v2(e.right) + eval_v2(e.left)
        }

        else -> {
            throw IllegalArgumentException("Unknown expression")
        }
    }

fun main() {
    println(eval(Sum(Sum(Num(1), Num(2)), Num(4))))
}