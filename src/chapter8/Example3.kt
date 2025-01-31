package chapter8

enum class Delivery { STANDARD, EXPEDITED }

class Order(val itemCount: Int)

fun getShippingCostCalculator(
    delivery: Delivery,
): (Order) -> Double {
    if (delivery == Delivery.EXPEDITED) {
        return { order -> 0.2 * order.itemCount }
    }

    return { order -> 1.2 * order.itemCount }
}

fun main() {
    val calculator = getShippingCostCalculator(Delivery.EXPEDITED)
    println("Shipping costs: ${calculator(Order(3))}")
}
