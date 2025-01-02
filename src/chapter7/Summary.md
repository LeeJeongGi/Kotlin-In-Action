## 연산자 오버로딩과 기타 관례

### 산술 연산자 오버로딩
- 자바에서는 원시 타입에 대해서만 산술 연산자를 사용할 수 있다. 추가로 String + 까지는 가능

### 이항 산술 연산 오버로딩

```kotlin
data class Point(
    val x: Int,
    val y: Int,
)

operator fun Point.plus(other: Point): Point {
    return Point(x + other.x, y + other.y)
}
```
- 함수 앞에 operator 키워드를 붙임으로써 어떤 함수가 관례를 따르는 함수임을 명확히 할 수 있다.
- 코틀린에서 오버로딩 가능한 이항 산술 연산자
  - a * b : times
  - a / b : div
  - a % b : mod
  - a + b : plus
  - a - b : minus

```kotlin
operator fun Point.times(scale: Double): Point {
    return Point((x * scale).toInt(), (y * scale).toInt())
}
```
- 연산자를 정의할 때 두 피연산자가 같은 타입일 필요는 없다.

```kotlin
operator fun Point.get(index: Int): Int {
    return when (index) {
        0 -> x
        1 -> y
        else -> throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}
```
- 그 밖의 여러가지 활용법들이다.

### 복합 대입 연산자 오버로딩
- +=, -= 등의 연산자는 복합 대입 연산자라 불린다.
- +, -는 항상 새로운 컬렉션을 반환한다
- +=, -= 연산자는 항상 변경 가능한 컬렉션에 작용해 메모리에 있는 객체 상태를 변화시킨다.

### 단항 연산자 오버로딩
- 단항 연산자 역시 이항 연산자와 동일한 방식으로 오버로딩 한다.
- operator로 표시하면 된다.
- 단항 연산자를 오버로딩하기 위해 사용하는 함수는 인자를 취하지 않는다.

```kotlin
operator fun Point.unaryMinus(): Point {
    return Point(-x, -y)
}
```

### 오버로딩할 수 있는 단항 산술 연산자
- +a : unaryPlus
- -a : unaryMinus
- !a : not
- ++a, a++ : inc
- --a, a-- : dec

### 비교 연산자 오버로딩
- 코틀린에서는 산술 연산자와 마찬가지로 원시 타입 값뿐 아니라 모든 객체에 대해 비교연산을 수행할 수 있다.
- 자바에서는 equals, compareTo를 호출해야 했다.
- 코틀린에서는 == 비교 연산자를 직접 사용할 수 있어서 간결하다

### 동등성 연산자: equals
- 코틀린이 == 연산자 호출은 equals 메소드 호출로 컴파일을 한다.
- a == b 라는 비교를 처리할 때 코틀린은 a가 널인지 판단해서 널이 아닌 경우에만 a.equals(b)를 호출한다.

### 순서 연산자: compareTo
- 자바에서 정렬이나 최댓값, 최솟값 등 값을 비교해야 하는 알고리즘에 사용할 클래스는 Comparable 인터페이스를 구현해야 한다.
- 코틀린도 똑같이 Comparable 인터페이스를 지원한다.
- 비교 연산자(>, <, >=, <=)는 compareTo 호출로 컴파일된다.

```kotlin
class Person(
    val firstName: String,
    val lastName: String,
) : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return compareValuesBy(this, other, Person::lastName, Person::firstName)
    }
}

fun main() {
    val p1 = Person("Alice", "Smith")
    val p2 = Person("Bob", "Johnson")
    println(p1 < p2)
}
```
- compareValuesBy는 두 객체와 여러 비교 함수를 인자로 받는다.
- 첫 번째 비교 함수에 두 객체를 넘겨서 두 객체가 같지 않다는 결과가 나오면 즉시 반환
- 두 객체가 같다는 결과가 나오면 두 번째 비교 함수를 통해 두 객체를 비교한다.

### 구조 분해 선언과 component 함수
- 구조 분해를 사용하면 복합적인 값을 분해해서 여러 다른 변수를 한꺼번에 초기화할 수 있다.

```kotlin
val point = Point(5, 5)
val (x, y) = point
println(x)
println(y)
```
- point x, y 좌표를 구조 분해를 통해 받을 수 있다.
- 구조 분해 선언의 각 변수를 초기화하기 위해 componentN이라는 함수를 호출한다.
- 여기서 N은 구조 분해 선언에 있는 변수 위치에 따라 붙는 번호이다.

```text
val (a, b) = p -> val a = p.component1()
                  val b = p.component2()
```
- 이런식으로 컴파일 된다.
- 컴파일러가 자동으로 componentN 함수를 만들어준다.

```kotlin
data class NameComponents(
    val name: String,
    val extension: String,
)

fun splitFilename(filename: String): NameComponents {
    val result = filename.split('.', limit = 2)
    return NameComponents(result[0], result[1])
}

fun main() {
    val (name, ext) = splitFilename("test.txt")
    println(name)
    println(ext)
}
```

```text
결과 
test
txt
```
- 무한정 componentN 함수를 만들어 주진 않는다.
- 앞에서 부터 5개까지만 함수를 만들어 준다.

### 구조 분해 선언과 루프
- 루프 안에서도 구조 분해 선언을 할 수 있다.

```kotlin
fun printEntries (map: Map<String, String>) {
    for ((key, value) in map) {
        println("$key: $value")
    }
}
```

### 위임 프로퍼티..
- 3번을 읽어봤지만 아직 잘 이해가 안간다.
- 언제 사용하는 걸까? 라는 의구심이 든다..
- 천천히 다시 기회가 된다면 볼 수 있겠지..?