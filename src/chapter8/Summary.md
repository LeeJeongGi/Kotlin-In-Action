## 고차 함수: 파라미터와 반환 값으로 람다 사용

### 고차 함수 정의
- 고차 함수는 다른 함수를 인자로 받거나 함수를 반환하는 함수이다.
- 코틀린에서는 함수를 값으로 표현할 수 있다.

### 함수 타입

```kotlin
val sum = {x: int, y: int -> x + y}
val action = { println(42) }
```
- 컴파일러는 sum, action이 함수 타입임을 추론한다.
- 이제 각 변수에 구체적인 타입 선언을 추가하면 어떻게 되는지 보자
```kotlin
val sum: (Int, Int) -> Int = {x, y -> x + y }
val action: () -> Unit = { println(42) }
```
- 함수 타입을 정의하려면 함수 파라미터의 타입을 괄호 안에 넣고, 그 뒤에 화살표를 추가한 다음, 함수의 반환 타입을 지정하면 된다.

### 인자로 받은 함수 호출
```kotlin
fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println("result: $result")
}

fun main() {
    val sum = {x: Int, y: Int -> x + y }
    println(sum(1, 2))

    println(twoAndThree(sum))
}
```
- twoAndThree가 sum이라는 술어를 받아서 내부에서 호출한다.
- 위에서 sum을 호출하는 방식과 twoAndThree(sum) 방식은 동일하다.

```kotlin
fun String.filter(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    for (index in 0 until length) {
        val element = get(index)
        if (predicate(element)) {
            sb.append(element)
        }
    }
    return sb.toString()
}
```
- String의 필터 확장 함수이다.
- predicate 파라미터는 문자를 파라미터로 받고 불리언 결과 값을 반환한다.
- 술어는 인자로 받은 문자가 filter 함수가 돌려주는 결과 문자열에 남아 있기를 바라면 true를 반환하고 문자열에서 사라지기를 바라면 false를 반환하면 된다.

### 디폴트 값을 지정한 함수 타입 파라미터나 널이 될 수 있는 함수 타입 파라미터

```kotlin
fun <T> Collection<T>.joinToString(
    collection: Collection<T>,
    separator: String = ",",
    prefix: String = "",
    postfix: String = "",
    transform: (T) -> String = { it.toString() },
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }

        result.append(transform(element))
    }

    result.append(postfix)
    return result.toString()
}

fun main() {
    val letters = listOf("Alpha", "Beta", "Gamma", "Delta", "Epsilon")
    println(letters.joinToString())

    println(letters.joinToString { it.lowercase() })
}
```
- transform 람다는 T 타입의 값을 인자로 받는다.
- 함수 타입의 디폴트 값을 선언할 때 특별한 구문이 필요하지는 않다.
- 함수 타입에 대한 디폴트 값 선언도 = 뒤에 람다를 넣으면 된다.

### 함수를 함수에서 반환

```kotlin
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
```
- 프로그램 상태나 다른 조건에 따라 달라질 수 있는 로직이 있다고 생각해보자.
- 함수 타입을 반환하려면 반환 타입으로 함수 타입을 지정해야 한다.

### 람다를 활용한 중복 제거
- 함수 타입과 람다 식은 재활용하기 좋은 코드를 만들 때 쓸 수 있는 훌륭한 도구이다.

```kotlin
data class SiteVisit(
    val path: String,
    val duration: Double,
    val os: OS
)

enum class OS {
    Windows, MacOS, Linux
}

fun main() {
    val log = listOf(
        SiteVisit("/", 30.0, OS.Windows),
        SiteVisit("/", 31.0, OS.MacOS),
        SiteVisit("/", 32.0, OS.Linux),
        SiteVisit("/", 33.0, OS.Linux),
        SiteVisit("/", 34.0, OS.Linux),
    )

    val average = log
        .filter { it.os == OS.Linux }
        .map(SiteVisit::duration)
        .average()
    println("average: $average")
}
```
- 리눅스 사용자에 대한 통계를 구하고 싶다.
- 람다를 활용해 간단한 표현식으로 구할 수 있다.

```kotlin
fun List<SiteVisit>.averageDurationFor(os: OS) =
    filter { it.os == os }.map(SiteVisit::duration).average()

val averageDuration = log.averageDurationFor(OS.MacOS)
println("average: $averageDuration")
```
- 확장 함수로 정의해서 더 간단하게 사용할 수 있다.
- 좀 더 가보자..
- 실제 상황에서는 더 복잡한 로직이 필요하다. 지금은 os로만 비교하지만 경로라던지.. 등등
- 여기서 함수 타입을 활용하면 필요한 조건을 파라미터로 뽑아낼 수 있다.

```kotlin
fun List<SiteVisit>.averageDurationFor2(predicate: (SiteVisit) -> Boolean) =
    filter(predicate).map(SiteVisit::duration).average()

val averageDuration2 = log.averageDurationFor2{
    it.os == OS.Linux && it.path == "/signup"
}
```
- 이렇게 filter 조건을 함수 타입으로 지정하여 동적으로 값을 계산할 수 있다.
- 진짜 말도 안되게 간편하지 않나요?..

### 인라인 함수: 람다의 부가 비용 없애기
- 람다를 활용한 코드의 성능은 어떨까?
- 람다식을 사용할 때마다 새로운 클래스가 만들어지지 않는다.
- 람다가 변수를 포획하면 람다가 생성되는 시점마다 새로운 무명 클래스 객체가 생긴다..
- 따라서 이런 경우에는 일반 함수를 사용하는 것보다는 덜 효율적이다.
- 그래서!! inline 변경자를 이용해 코틀린에서는 어떻게 활용되는지 알아보자

### 인라이닝이 작동하는 방식

```kotlin
inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    } 
}
```
- java의 synchronized문과 똑같아 보인다.
- 차이는 자바에서는 임의의 객체에 대해 사용할 수 있지만 이 함수는 Lock 클래스의 인스턴스를 요구한다.
- synchronized 함수를 inline을 선언 했기 때문에 자바의 synchronized문과 같아진다.

```kotlin
fun foo(l: Lokc) {
    println("Before sync")
    
    synchronized(l) {
        println("Action")
    }
    
    println("After sync")
}
```
- 아래 코드는 위 코드랑 동등한 코드를 보여준다.
- 이 코드는 앞의 코드와 같은 바이트코드를 만들어낸다.
```kotlin
fun foo(l: Lokc) {
    println("Before sync")
    
    lock.lock()
    try {
        println("Action")
    } finally {
        lock.unlock()
    }
    
    println("After sync")
}
```
- synchronized에 전달된 람다의 본문도 함께 인라이닝된다는 점에 유의하자.
- 람다의 본문에 의해 만들어지는 바이트코드는 그 라다를 호출하는 코드 정의의 일부분으로 간주되기 때문에 코틀린 컴파일러는 그 람다를 함수 인터페이스를 구현하는 무명 클래스로 감싸지 않는다.

### 인라인 함수의 한계
- 인라이닝을 하는 방식으로 인해 람다를 사용하는 모든 함수를 인라이닝할 수는 없다.
- 함수 본문에서 파라미터로 받은 라다를 호출한다면 그 호출을 쉽게 람다 본문으로 바꿀 수 있다.
- 하지만 파라미터로 받은 람다를 다른 변수에 저장하고 나중에 그 변수를 사용한다면 람다를 표현하는 객체가 어딘가는 존재해야 하기 때문에 람다를 인라이닝할 수 없다.

```kotlin
fun <T, R> Sequence<T>.map(transform: (T) -> R): Sequence<R> {
    return TransformingSequence(this, transform)
}
```
- 여기서 transform을 호출하지 않고 TransformingSequence 파라미터로 전달한다.
- 이럴 경우 인라이닝하지 않는 일반적인 함수 표현으로 밖에 만들 수가 없다.
- 여기서는 transform을 함수 인터페이스를 구현하는 무명 클래스 인스턴스로 만들어야만 한다.

### 컬렉션 연산 인라이닝
- 코틀린에서 컬렉션에 대한 연산은 안전하게 사용할 수 있다.
- 즉 코틀린이 제공하는 함수 인라이닝을 믿고 성능에 신경 쓰지 않아도 된다.
- 만약 filter와 map을 동시에 사용하는 경우에는 어떨까?
- filter, map 모두 인라인 함수다.

```kotlin
people.filter { it.age < 30}
    .map(Person::name)
```
- 이 코드에서는 필터에서 걸러낸 결과를 저장하는 중간 리스트를 만든다.
- 처리할 원소가 많아지면 중간 리스트를 사용하는 부가 비용도 걱정할 만큼 커진다.
- asSequence를 통해 리스트 대신 시퀀스를 사용하면 중간 리스트로 인한 부가 비용은 줄어든다.
- 이때 각 중간 시퀀스는 람다를 필드에 저장하는 객체로 표현되며, 최종 연산은 중간 시퀀스에 있는 여러 람다를 연쇄 호출한다.
- 따라서 앞에서 설명한 대로 시퀀스는 람다를 인라인하지 않는다.
- 따라서!!! 지연 계산을 통해 성능을 향상시키려는 이유로 모든 컬렉션 연산에 asSequence를 붙여서는 안 된다.
- 시퀀스 연산에서는 라다가 인라이닝되지 않기 때문에 크기가 작은 컬렉션은 오히려 일반 컬렉션 연산이 더 성능이 나을 수 있다.
- 시퀀스를 통해 성능을 향상시킬 수 있는 경우는 컬렉션 크기가 큰 경우 뿐이다.

### 함수를 인라인으로 선언해야 하는 경우
- 인라이닝을 통해 없앨 수 있는 부가 비용이 상당하다.
  - 함수 호출 비용을 줄일 수 있을 뿐만 아니라 람다를 표현하는 클래스와 람다 인스턴스에 해당하는 객체를 만들 필요도 없어진다.
- 현재의 JVM은 함수 호출과 람다를 인라이닝해 줄 정도로 똑똑하지는 못하다
- 인라이닝을 사용하면 일반 람다에서는 사용할 수 없는 몇 가지 기능을 사용할 수 있다.
  - 뒤에 나오지만 대표적으로 non-local 반환이 있다.
- 하지만 inline 변경자를 함수에 붙일 때는 코드 크기에 주의를 기울여야 한다.
- 인라이닝하는 함수가 클 경우 함수의 본문에 해당하는 바이트코드를 모든 호출 지점에 복사해 넣고 나면 바이트코드가 전체적으로 아주 커질 수 있다.

### 람다 안의 return문: 람다를 둘러싼 함수로부터 반환

```kotlin
data class Person2(
    val name: String,
    val age: Int,
)

fun lookForAlice(persons: List<Person2>) {
    for (person in persons) {
        if (person.name == "Alice") {
            println("Found!!")
            return
        }
    }

    persons.forEach {
        if (it.name == "Alice") {
            println("Found!!")
            return
        }
    }
    println("Alice is not found") // Alice 가 없는 경우에만 출력
}

fun main() {
    val persons = listOf(Person2("Alice", 25), Person2("dudu", 25))
    lookForAlice(persons)
}
```
- for문과 forEach문 모두 동일하게 동작 한다.
- 람다 안에서 return을 사용 하면 람다로부터만 반환되는 게 아니라 그 람다를 호출하는 함수가 실행을 끝내고 반환한다.
- 자신을 둘러싸고 있는 블록보다 더 바깥에 있는 다른 블록을 반환하게 만드는 return문을 넌로컬(non-local) return이라 부른다.
- forEach가 인라인 함수라 가능한 일이다.
- 인라이닝되지 않는 함수라면 넌로컬이 되지 않는다.

### 람다로부터 반환: 레이블을 사용한 return
```kotlin
fun lookForAlice(persons: List<Person2>) {
    persons.forEach label@{
        if (it.name == "Alice") {
            return@label
        }
    }
    println("Alice is not found") // 항상 이 줄이 출력된다.
}
```
- 람다 식에서도 로컬 return을 사용할 수 있다.
- 람다 안에서 로컬 return은 for 루프의 break와 비슷한 역할을 한다.
- 로컬 return은 람다의 실행을 끝내고 람다를 호출했던 코드의 실행을 계속 이어간다.
