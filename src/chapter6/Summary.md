## 코틀린 타입 시스템

- 자바와 비교하면 코틀린의 타입 시스템은 코드의 가독성을 향상시키는 데 도움이 되는 몇 가지 특징을 새로 제공한다.
  - 널이 될 수 있는 타입
  - 읽기 전용 컬렉션

### 널 가능성
- NPE(Null Point Exception)를 피할 수 있게 돕디 위한 코틀린 특성이다.
- 널이 될 수 있는지 여부를 타입 시스템에 추가함으로써 컴파일러가 여러 가지 오류를 컴파일 시 미리 감지해서 실행 시점에 발생 할 수 있는 예외의 가능성을 줄일 수 있다.

### 널이 될 수 있는 타입
- 프로그램 안의 프로퍼티나 변수에 null 허용하게 만드는 방법

```java
int strLen(String s) {
    return s.length();
}
```
- java 에서는 파라미터 s에 null이 넘어오면 NPE가 발생한다.

```kotlin
fun strLen(s: String) = s.length
```
- 코틀린에서는 맨 처음 널 값을 받을 수 있는지 확인한다.
- 만약 null 값을 넘기게 되면 컴파일 시점에 에러가 발생한다.
- 이 함수에 null 값을 허용하기 위해선 ? 를 추가해야 한다.

```kotlin
fun strLen(s: String?) = s.length
```
- Type 뒤에 ?를 추가하면 null 참조를 저장할 수 있다는 뜻이다.
- null 참조가 추가 되면 제약 사항이 여러 가지 생기게 된다.
- 만약 널 가능성을 다루기 위한 도구가 If문 뿐이라면 코드가 번잡해 질 것이다.

### 안전한 호출 연산자: ?.
- 코틀린이 제공하는 유용한 도구
- ?.은 null 검사와 메소드 호출을 한 번의 연산으로 수행한다.
```kotlin
s?.toUpperCase()

if (s != null) s.toUpperCase()
```
- 두 개의 식은 같은 의미를 가진다.

### 엘비스 연산자: ?:
- null 대신 사용할 디폴트 값을 지정할 때 편리하게 사용할 수 있는 연산자를 제공한다.
```kotlin
fun foo(s: String) {
    val t: String = s ?: ""
}
```
- s가 null 이면 디폴트 "" 값이 들어간다.

### 안전한 캐스트: as?
- as? 연산자는 어떤 값을 지정한 타입으로 캐스트한다.

```kotlin
class Person(
    val firstName: String,
    val lastname: String,
) {
    override fun equals(other: Any?): Boolean {
        val otherPerson = other as? Person ?: return false
        return otherPerson.firstName == firstName && otherPerson.lastname == lastname
    }

    override fun hashCode(): Int {
        var result = firstName.hashCode()
        result = 31 * result + lastname.hashCode()
        return result
    }
}
```
- equals() 메서드에서 유용하게 쓰인다.

### 널 아님 단언: !!
- 느낌표를 이중으로 사용하면 어떤 값이든 널이 될 수 없는 타입으로 바꿀 수 있다.
- 실제 널이 발생하면 NPE가 발생한다.

```kotlin
fun ignoreNulls(s: String?) {
    val sNotNull: String = s!!
    println(s.length)
}
```
- s가 null이 되면 NPE 발생.
- 이런 오류 상황이 생길 수 있기 때문에 상황을 잘 판단해서 사용해야 한다.

### let 함수
- let 함수를 사용하면 널이 될 수 있는 식을 더 쉽게 다룰 수 있다.
- 사용 용례는 널이 될 수 있는 값을 널이 아닌 값만 인자로 받는 함수에 넘기는 경우

```kotlin
fun sendEmailTo(email: String) {/* 작업 진행.. */}

fun main() {
    val test: String?
    test?.let {
        sendEmailTo(it)
    }
}
```
- 이런식으로 널이 아닌 값만 sendEmailTo를 호출 할 수 있게 사용한다.

### 나중에 초기화할 프로퍼티
- 나중에 초기화하는 프로퍼티는 항상 var여야 한다.
  - 왜냐하면 val은 final 이기 때문에 항상 초기화가 필요하기 때문이다.
  - 그래서 나중에 초기화하는 프로퍼티는 널이 될 수 없는 타입이라도 생성자 안에서 초기화할 필요가 없다.

### 널이 될 수 있는 타입 확장
- 널이 될 수 있는 타입에 대한 확장 함수를 정의하면 null 값을 다루는 강력한 도구가 될 수 있다.

```kotlin
fun verifyUserInput(input: String?) {
    if (input.isNullOrBlank()) {
        println("Input is empty")
    }
}

println(verifyUserInput("ttt"))
println(verifyUserInput(""))
println(verifyUserInput(null))
```
- isNullOrBlank 메소드는 널을 명시적으로 검사해서 널인 경우 true를 반환하고, 널이 아닌 경우 isBlank를 호출해 준다.

### 타입 파라미터의 널 가능성
- 코틀린에서는 함수나 클래스의 모든 타입 파라미터는 기본적으로 널이 될 수 있다.
- 타입 파라미터 T를 클래스나 함수 안에서 타입 이름으로 사용하면 이름 끝에 물음표가 없더라도 T가 널이 될 수 있는 타입이다.

```kotlin
fun <T> printHashCode(t: T) {
    println(t?.hashCode())
}

>> printHashCode(null)
null
```
- t 파라미터에는 ?가 없지만 null이 들어 갈 수 있다.
- 따라서 널이 아님을 확실히 하려면 널이 될 수 없는 타입 상한을 지정해야 한다.
- T의 타입은 Any? 이기 때문에 null 가능
```kotlin
fun <T: Any> pringHashCode(t: T) {
    println(t.hashCode())
}

>> printHashCode(null) : Error: Type Parameter bound for 'T' is not satisfied
```
- 이 코드는 컴파일이 되지 않는다.

### 널 가능성과 자바
- 자바와 코틀린을 같이 쓰게 되면 널 가능성을 처리하지 않는 자바 코드를 어떻게 처리 할까?
  - @NotNull, @Nullable 어노테이션이 있으면 활용한다.
  - 어노테이션이 없다면 자바의 타입은 `코틀린 플랫폼 타입`이 된다.

`플랫폼 타입`
- 코틀린이 널 관련 정보를 알 수 없는 타입
- 널이 될 수도 있고 안 될 수도 있음
- 즉, 수행하는 모든 연산에 대한 책임은 온전히 우리에게 있다라는 뜻이다.
- 코틀린에서 플랫폼 타입을 선언할 수는 없다.
- 자바 코드에서 가져온 타입만 플랫폼 타입이 된다.
- 만약 코틀린 컴파일러가 오류 메세지에 String!이라고 표시하면 이게 바로 자바에서 온 타입이라는 뜻이다.

### 원시 타입: Int, Boolean
- 코틀린에서는 원시 타입과 래퍼 타입을 구분하지 않는다.
- 대부분의 경우 코틀린의 Int 타입은 자바 int 타입으로 컴파일 된다.
- 컬렉션과 같은 제네릭 클래스를 사용하는 경우를 제외 하고!!

### 널이 될 수 있는 원시 타입: Int?, Boolean?
- null 참조를 자바의 참조 타입의 변수에만 대입할 수 있기 때문에 널이 될 수 있는 코틀린 타입은 자바 원시 타입으로 표현할 수 없다.
- 그 타입은 자바의 래퍼 타입으로 컴파일된다.
- JVM 타입 인자로 원시 타입을 허용하지 않는다.
- 따라서 자바나 코틀린 모두에서 제네릭 클래스는 항상 박스 타입을 사용해야 한다.

### 숫자 변환
- 코틀린과 자바의 가장 큰 차이점 중 하나는 숫자를 변환하는 방식이다.
- 코틀린은 한 타입의 숫자를 다른 타입의 숫자로 자동 변환하지 않는다.
- 대신 모든 원시 타입에 대한 변환 함수를 제공한다.
- 그래서 코틀린에서는 값 넘침 현상이 발생하지 않는다. (long 타입을 int로 자동 변환해서 생기는 일)

### Any, Any?: 최상위 타입
- 자바의 object 처럼 최상위 타입을 뜻한다.
- 자바에서는 원시 타입의 조상은 object가 아니지만 코틀린에서는 원시 타입의 조상도 Any이다.

### Unit 타입: 코틀린의 void
- Unit은 모든 기능을 갖는 일반적인 타입이다.
- void와 달리 Unit을 타입 인자로 쓸 수 있다.

```kotlin
interface Processor<T> {
    fun process() : T
}

class NoResultProcessor : Processor<Unit> {
    override fun process() {
        // 업무 처리 코드
    }
}
```
- 이렇게 Unit 타입을 지정하면 컴파일러가 묵시적으로 return Unit을 넣어준다.
- 타입 인자로 값 없음을 표현하는 문제를 코틀린에서는 깔끔하게 해결하고 있다.

### Noting 타입: 이 함수는 결코 정상적으로 끝나지 않는다.

```kotlin
fun fail(message: String) : Nothing {
    throw IllegalStateException(message)
}

>> fail("Error occurred")
```
- Nothing 타입은 아무 값도 포함하지 않는다.
- 따라서 반환 타입이나 반환 타입으로 쓰일 타입 파라미터로만 쓸 수 있다.

### 널 가능성과 컬렉션
- List<Int?>? 이렇게 되어 있을 때 타입 인자에 있는 ?와 컬렉션 뒤에 있는 ? 의 차이를 구분할 수 있어야 한다.
  - Int? 의미는 List 안에 Int 또는 null이 저장 가능하다는 의미이다.
  - List<Int>? 는 List 자체 컬렉션이거나 null이 될 수 있다는 의미를 가지고 있다.

### 읽기 전용과 변경 가능한 컬렉션
- 코틀린에서는 컬렉션 안의 데이터에 접근하는 인터페이스와 컬렉션 안의 데이터를 변경하는 인터페이스를 분리 했다.
- Collection 인터페이스를 사용하면 크기를 구하거나 안에 있는 값을 구하거나 하는 읽는 연산을 할 수 있다.
  - 하지만, 원소를 추가하거나 제거하는 메소드는 없다.
- MutableCollection 인터페이스는 데이터를 수정하려면 사용해야 한다.
- MutableCollection 인터페이스는 기존의 Collection 인터페이스에 원소를 추가하거나 삭제하는 연산을 확장한 것이다.

### 코틀린 컬렉션과 자바
- 코틀린은 자바 컬렉션 인터페이스마다 읽기 전용 인터페이스와 변경 가능한 인터페이스라는 두 가지 표현을 제공한다.








