## 함수 정의와 호출

### 코틀린에서 컬렉션 만들기

- 코틀린에서 컬렉션 만드는 방법
```kotlin
val set = hashSetOf(1, 7, 53)
val list = arrayListOf(1, 7, 53)
val map = hashMapOf(1 to "one", 7 to "seven", 53 to "fifty-three")

println(set.javaClass)
println(list.javaClass)
println(map.javaClass)
```

```text
* 호출 결과

class java.util.HashSet
class java.util.ArrayList
class java.util.HashMap
```
- 호출 결과를 보게 되면 코틀린이 자신만의 컬렉션 기능을 제공하지 않는다는 이유를 알 수 있다.
- 코틀린이 자체 컬렉션을 제공하지 않는 이유는 ?
  - 표준 자바 컬렉션을 활용하면 자바 코드와 상호작용하기가 훨씬 쉽기 때문이다.
  - 다만, 추가적인 기능이 더 많다.

### 함수를 호출하기 쉽게 만들기

- 자바 컬렉션에는 디폴트 toString 구현이 들어있다. 하지만 그 디폴트 toString의 출력 형식은 고정돼 있고 우리에게 필요한 형식이 아닐 수도 있다.
- 코틀린으로 컬렌션을 콘솔에 나타낼 때 커스템하는 코드를 만들어 보자.

```kotlin
fun <T> Collection<T>.joinToString(
    separator: String = ",",
    prefix: String = "",
    postfix: String = "",
): String {
    val result = StringBuilder(prefix)

    for ((index, element) in this.withIndex()) {
        if (index > 0) {
            result.append(separator)
        }

        result.append(element)
    }

    result.append(postfix)
    return result.toString()
}
```

```kotlin
val list = listOf(1, 2, 3, 4, 5)
println(list.joinToString(separator = "; ", prefix = "(", postfix = ")"))
```

```text
* 호출 결과

(1; 2; 3; 4; 5)
```

- joinToString에 사실 많은 기법이 들어가 있다.
- 디폴트 파라미터 값을 설정했다.
  - 자바에서는 오버로딩 메소드들이 많이 만들어 진다. 편의를 제공하기 위해서.
  - 하지만 결국 이것도 중복이다.
  - 코틀린에서는 이런 중복을 피하기 위해 default 값을 지정 할 수 있다.
  - 따라서 `println(list.joinToString(separator = "; "))` 이런식으로 호출하여 사용 할 수도 있다.
- Collection<T>.joinToString 확장 함수로 정의했다.

`확장 함수`
- 코틀린에서 기존 자바 코드를 통합하는 경우에는 코틀린으로 직접 변환할 수 없거나 미처 변환하지 않은 기존 자바 코드를 처리할 수 있어야 한다.
- 확장 함수가 바로 그런 역할을 해준다.

```kotlin
fun String.lastChar(): Char = this[this.length - 1]
```
- 수신 객체 타입(String.)은 확장이 정의될 클래스의 타입이며, 수신 객체(this)는 그 클래스에 속한 인스턴스 객체이다.

```kotlin
println("Kotlin".lastChar())
```
- 정의한 확장 함수를 이렇게 사용하면 된다.
- 확장 함수는 단지 정적 메소드 호출에 대한 문접적인 편의일 뿐이다.

### 확장 함수는 오버라이드할 수 없다

```kotlin
fun View.showOf() = println("I'm a view")
fun Button.showOf() = println("I'm a button")

fun main() {
    val view: View = Button()
    view.showOf()
}
```

```text
* 호출 결과

I'm a view
```
- 확장 함수는 클래스의 일부가 아니다.
- 확장 함수는 클래스의 밖에서 선언된다.
- view가 가리키는 객체의 실제 타입이 Button이지만, 이 경우 view의 타입이 View이기 때문에 무조건 View의 확장 함수가 호출 된다.


### 확장 프로퍼티

```kotlin
val String.lastChar: Char
    get() = get(length - 1)

var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }

fun main() {
    println("kotlin".lastChar)

    val sb = StringBuilder("kotlin?")
    println(sb)

    sb.lastChar = '!'
    println(sb)
}
```

```text
* 호출 결과

n
kotlin?
kotlin!
```
- 기본 게터 구현을 제공할 수 없으므로 최소한 게터는 꼭 정의를 해야 한다.
- var 사용하면 set도 구현 가능하다.

### 자바 컬렉션 API 확장
- 앞에 예제들을 작성해보면서 리스트의 마지막 원소를 가져오는 예제와 숫자로 이뤄진 컬렉션의 최댓값을 찾는 예제를 살펴봤다.
- 이제는 이게 어떻게 작동하는지 정확히 알 것이다.
- last와 max는 모두 확장 함수였던 것이다!!!

```kotlin
fun <T> List<T>.last(): T { /* 마지막 원소를 반환함 */}
fun Collection<Int>.max(): Int {/* 컬렉션의 최댓값을 찾음 */}
```

### 가변 인자 함수: 인자의 개수가 달라질 수 있는 함수 정의
- 리스트를 생성하는 함수를 호출할 때 원하는 만큼 많이 원소를 전달할 수 있다.

```kotlin
fun listOf<T> (vararg values: T): List<T> { ... }
```
- `vararg` 가변 길이 인자 키워드
- 파라미터 앞에 `vararg` 변경자를 붙인다.

### 값의 쌍 다루기: 중위 호출과 구조 분해 선언

```kotlin
val maps = mapOf(1 to "one", 2 to "two", 3 to "three")
```
- 여기서 to 키워드는 코틀린 키워드가 아니다.
- 이 코드는 `중위 호출`이라는 특별한 방식으로 to라는 일반 메소드를 호출한 것이다.
```text
1.to("one"): "to" 메소드를 일반적인 방식으로 호출
1 to "one": "to" 메소드를 중위 호출 방식으로 호출
```

```kotlin
public infix fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)
```
- to 함수는 Pair의 인스턴스를 반환한다.
- 함수를 중위 호출에 사용하게 허용하고 싶으면 infix 변경자를 함수 선언 앞에 추가해야 한다.

```kotlin
val (number, name) = 1 to "one"
```
- 이런 기능을 구조 분해 선언이라고 부른다.

### 로컬 함수와 확장

```kotlin
class User(
    val id: Int,
    val name: String,
    val address: String,
)

fun User.validateBeforeSave() {

    fun validate(value: String, fieldName: String) {
        if (value.isEmpty()) {
            throw IllegalArgumentException(
                "Can't save user ${id}: empty $fieldName"
            )
        }
    }

    validate(name, "Name")
    validate(address, "Address")
}

fun saveUser(user: User) {
    user.validateBeforeSave()
}

fun main() {
    saveUser(User(1, "", ""))
}
```
- User 클래스를 저장하기 전에 값을 검증하는 확장 함수를 정의 했다.
- 확장 함수안에 또 다른 함수를 정의해서 캡슐화를 시켰다. (반복되는 코드를 줄이기 위해서)
- saveUser() 메소드를 호출하면 값에 대한 검증을 진행하고 save를 진행하면 된다.