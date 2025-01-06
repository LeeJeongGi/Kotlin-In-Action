## 제네릭스

### 제네릭 타입 파라미터
- 제네릭 타입의 인스턴스를 만들려면 타입 파라미터를 구체적인 타입 인자로 치환해야 한다.
- 코틀린 컴파일러는 보통 타입과 마찬가지로 타입 인자도 추론할 수 있다.
- 반면에 빈 리스트를 만들어야 한다면 타입 인자를 추론할 근거가 없기 때문에 직접 타입 인자를 명시해야 한다.
```kotlin
val readers: MutalbleList<String> = mutalbleListOf()
val readers = mutalbleListOf<String>()
```

### 제네릭 함수와 프로퍼티
- 컬렉션을 다루는 라이브러리 함수는 대부분 제네릭 함수다.

```kotlin
fun <T> List<T>.slice(indices: IntRange) : List<T> // fun <T> << 타입 파라미터 선언
```
- 함수의 타입 파라미터 T가 수신 객체와 반환 타입에 쓰인다.
- 따라서 컴파일러가 타입 인자를 추론할 수 있다.

### 제네릭 클래스 선언
- 코틀린에서도 타입 파라미터를 넣은 꺾쇠 기호를 클래스 이름 뒤에 붙이면 클래스를 제네릭하게 만들 수 있다.
- 타입 파라미터를 이름 뒤에 붙이고 나면 클래스 본문 안에서 타입 파라미터를 다른 일반 타입처럼 사용할 수 있다.

### 타입 파라미터 제약
- `타입 파라미터 제약`은 클래스나 함수에 사용할 수 있는 타입 인자를 제한하는 기능이다.
- sum 함수는 List<Int>, List<Double>에 그 함수를 적용할 수 있지만 List<String>등에는 적용할 수 없다.

```kotlin
fun <T: Number> List<T>.sum(): T
```
- 제약을 가하려면 타입 파라미터 이름 뒤에 클론(:)을 표시하고 그 뒤에 상한 타입을 적으면 된다.
- T에 대한 상한을 정하고 나면 T 타입의 값을 그 상한 타입의 값으로 취급 할 수 있다.

### 타입 파라미터를 널이 될 수 없는 타입으로 한정
- 아무런 상한을 정하지 않은 타입 파라미터는 결과적으로 Any?를 상한으로 정한 파라미터와 같다

```kotlin
class Processor<T>{
    fun process(value: T) {
        value?.hashCode() // value는 널이 될 수 있다.
    }
}
```
- process 함수에서 value 파라미터의 타입 T에는 물음표(?)가 붙어있지 않지만 실제로는 T에 해당하는 타입 인자로 널이 될 수 있는 타입을 넘길 수 있다.
- 널 가능성을 제외한 아무런 제약도 필요 없다면 Any? 대신 Any를 상한으로 사용해야 한다.

```kotlin
class Processor<T : Any>{
    fun process(value: T) {
        value?.hashCode() // value는 널이 될 수 있다.
    }
}
```
- <T : Any>라는 제약은 T 타입이 항상 널이 될 수 없는 타입이 되게 보장한다.

### 실행 시 제네릭스의 동작: 소거된 타입 파라미터와 실체화된 타입 파라미터
- JVM의 제네릭스는 보통 타입 소거를 사용해 구현된다.
- 이는 실행 시점에 제네릭 클래스의 인스턴스에 타입 인자 정보가 들어있지 않다는 뜻이다.
- 함수를 inline으로 만들면 타입 인자가 지워지지 않게 할 수 있다.

### 실행 시점의 제네릭: 타입 검사와 케스트
- 자바와 마찬가지로 코틀린 제네릭 타입 인자 정보는 런타임에 지워진다.

```kotlin
val list1 = listOf("a", "b")
val list2 = listOf(1, 2, 3)
```
- 컴파일러는 두 리스트를 서로 다른 타입으로 인식하지만 실행 시점에 그 둘은 완전히 같은 타입의 객체다.
- 그럼에도 불구하고 보통은 List<String>에는 문자열만 들어있고 List<Int>에는 정수만 들어있다고 가정할 수 있다.
- 이는 컴파일러가 타입 인자를 알고 올바른 타입의 값만 각 리스트에 넣도록 보장해주기 때문이다!!
- 저장해야 하는 타입 정보의 크기가 줄어들어서 전반적인 메모리 사용량이 줄어든다는 제네릭 타입 소거 나름의 장점이 있다.
- 하지만 inline 함수를 사용하면 타입 인자를 사용할 수 있다.

### 실체화한 타입 파라미터를 사용한 함수 선언
- 인라인 함수의 타입 파라미터는 실체화되므로 실행 시점에 인라인 함수의 타입 인자를 알 수 있다.

```kotlin
inline fun <reified T> isA(value: Any) = value is T
println(isA<String>("abc")) // true
println(isA<String>(123)) // false
```
- 함수를 inline 함수로 만들고 타입 파라미터를 reified로 지정하면 value 타입이 T의 인스턴스인지를 실행 시점에 검사할 수 있다.

```kotlin
val items = listOf("one", 2, "three")
println(items.filterIsInstance<String>())
>> [one, three]
```
- filterIsInstance 타입 인자로 String을 지정함으로써 문자열만 필요하다는 사실을 기술한다.
- filterIsInstance 코틀린에서 제공하는 라이브러리 함수이다.

```kotlin
inline fun <reified T> Iterable<*>.filterIsInstance(): List<T> {
    val destination = mutableListOf<T>()
    for (element in this) {
        if (element is T) {
            destination.add(element)
        }
    }
    
    return destination
}
```
- 표준 라이브러리에 있는 filterIsInstance 선언을 간단하게 정리한 코드이다.
- 컴파일러는 인라인 함수의 본문을 구현한 바이트코드를 그 함수가 호출되는 모든 지점에 삽입한다.
- 컴파일러는 실체화한 타입 인자를 사용해 인라인 함수를 호출하는 각 부분의 정확한 타입 인자를 알 수 있다.
- 따라서 컴파일러는 타입 인자로 쓰인 구체적인 클래스를 참조하는 바이트코드를 생성해 삽입할 수 있다.
- 타입 파라미터가 아니라 구체적인 타입을 사용하므로 만들어진 바이트코드는 실행 시점에 벌어지는 타입 소거의 영향을 받지 않는다.

### 실체화한 타입 파라미터의 제약
- 타입 파라미터 클래스의 인스턴스 생성하기
- 타입 파라미터 클래스의 동반 객체 메서드 호출하기
- 실체화한 타입 파라미터를 요구하는 함수를 호출하면서 실체화하지 않은 타입 파라미터로 받은 타입을 타입 인자로 넘기기
- 클래스, 프로퍼티, 인라인 함수가 아닌 함수의 타입 파라미터를 reified로 지정 하기

### 변성: 제네릭과 하위 타입
- 변성 개념은 List<String>와 List<Any>와 같이 기저 타입이 같고 타입 인자가 다른 여러 타입이 서로 어떤 관계가 있는지 설명하는 개념이다.

### 변성이 있는 이유: 인자를 함수에 넘기기
- List<Any> 타입의 파라미터를 받는 함수에 List<String>을 넘기면 안전할까?
  - String 클래스는 Any를 확장하므로 Any 타입 값을 파라미터로 받는 함수에 String 값을 넘겨도 절대로 안전한다.
- 하지만 List 인터페이스의 타입 인자로 들어가는 경우 그렇게 자신 있게 안전성을 말할 수 없다.

```kotlin
fun addAnswer(list: MutableList<Any>) {
    list.add(42)
}

fun main() {
    val strings = mutableListOf("abc", "bac")
    addAnser(strings)
    println(strings.maxBy {it.length}) // ClassCastException 발생
}
```
- 정수를 문자열 리스트 뒤에 추가할 수 있다.
- maxBy 호출할 때 에러 발생
- 따라서 MutableList<Any>가 필요한곳에 MutableList<String>을 넘기면 안된다는 사실을 보여준다.
- 리스트를 읽기만 한다면 상관 없지만 쓰기하는 경우 문제가 생긴다.

### 클래스, 타입, 하위 타입
- 클래스와 타입은 다른 개념이다.

```kotlin
var x: String
var x: String?
```
- kotlin에서는 이렇게 String 타입과 널이 될 수 있는 String 타입 항상 두 개의 형태를 가진다.
- `하위 타입`은 어떤 타입 A의 값이 필요한 모든 장소에 어떤 타입 B의 값을 넣어도 아무 문제가 없다면 타입 B는 타입 A의 하위 타입이다.
- 제네릭 타입을 인스턴스화할 때 타입 인자로 서로 다른 타입이 들어가면 인스턴스 타입 사이의 하위 타입 관계가 성립하지 않으면 그 제네릭 타입을 `무공변`이라고 한다.
- 반대로, A.가 B.의 하위 타입이면 List<A.>는 List<B.>의 하위 타입이다. 그런 클래스나 인터페이스를 `공변적`이라 말한다.

### 공변성: 하위 타입 관계를 유지

```kotlin
interface Producer<out T>{
    fun produce(): T
}
```
- 제네릭 클래스가 타입 파라미터에 대해 공변적임을 표시하려면 타입 파라미터 이름 앞에 out을 넣어야 한다.

```kotlin
open class Animal {
    fun feed() { /**/ }
}

class Herd<T : Animal>{
    val size: Int get() = /*...*/
    operator fun get(i: Int): T {/*...*/}
}

fun feedAll(animals: Herd<Animal>) {
    for (i in 0 until animals.size) {
        animals[i].feed()
    }
}
```

```kotlin
class Cat : Animal() {
    fun cleanLitter() {/*...*/}
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for (i in 0 until cats.size) {
        cats[i].cleanLitter()
        feedAll(cats) // 에러 발생
    }
}
```
- feedAll(cats) 호출하면 에러가 난다.
- 왜냐하면 Herd 클래스의 T 타입 파라미터에 대해 아무 변성도 지정하지 않았기 때문에 고양이 무리는 동물 무리의 하위 클래스가 아니다.
- 따라서 공변적으로 바꿔야 한다.

```kotlin
class Herd<out T : Animal>{
    /*...*/
}
```
- 이렇게 수정하면 feedAll(cats) 호출 가능

### 반공변성: 뒤집힌 하위 타입 관계
- 타입 B가 타입 A의 하위 타입인 경우 Consumer<A.>가 Consumer<B.>의 하위 타입인 관계가 성립하면 제네릭 클래스 Consumer<T>는 타입 인자 T에 대해 반공변이다.

```kotlin
interface Function1<in P, out R>{
    operator fun invoke(p: P): R
}
```
- P는 오직 in 위치, R은 오직 out 위치에 사용된다.
- 이 관계는 첫 번째 타입 인자의 하위 타입 관계와는 반대지만 두 번째 타입 인자의 하위 타입 관계와는 같음을 뜻한다.
