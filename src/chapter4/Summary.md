## 클래스, 객체, 인터페이스

### 클래스 계층 정의
- 코틀린과 자바의 Default 가시성은 다르다.

```kotlin
interface Clickable {
    fun click()
    fun showOff() = println("I'm clickable!") /* Default Method */
}
```
- Clickable 인터페이스 구현체들은 click 메서드를 오버라이드 해야한다.
- 자바에서는 extends, implement를 사용하지만 코틀린에서는 클래시 이름 뒤에 [: 인터페이스 이름] 을 적으면 된다.

```kotlin
class Button : Clickable {
    override fun click() = println("I was Click")
}
```
- 인터페이스 메소드도 디폴트 구현을 제공할 수 있다.
- 디폴트 메서드는 구현을 오버라이드 해도 되고 안해도 된다.
- 만약, 두개의 인터페이스에서 동일한 이름을 가진 디폴트 메서드가 있을 때는 어떤 로직을 선택할까?
  - 어느 쪽도 선택되지 않는다. 컴파일 오류 발생하기 때문에..
  - 하위 클래스에서 직접 구현하게 강제함..
  ```kotlin
    override fun click() = {
        super<Clickable>.showOff()
        super<Focusable>.showOff()
    }
    ```
  - Clickable, Focusable 두 개의 인터페이스를 상속 받을 경우 위와 같이 super<기반 타압>.메서드명 이렇게 사용해야 한다.

### open, final, abstract 변경자: 기본적으로 final
- 자바에서는 final로 명시적으로 상속을 금지하지 않는 모든 클래스를 다른 클래스가 상속할 수 있다.
  - `취약한 기반 클래스`라는 문제가 생긴다.
  - 어렵게 생각하지 말자. 상위 클래스를 변경하면 하위 클래스도 같이 변경해야 하기 때문에 상위 클래스에 결합이 생기는 문제를 뜻한다.
  - 이러한 이유 때문에 이펙티브 자바에서는 이렇게 설명 한다고 한다.
  - `상속을 위한 설계와 문서를 갖추거나, 그럴 수 없다면 상속을 금지하라`
  - 즈윽, 하위 클래스에서 오버라이드하게 의도된 클래스와 메소드가 아니라면 모두 final로 만들자!!


- 코틀린도 이러한 철학을 따른다.
- 자바는 기본적으로 상속에 열려있지만, 코틀린에서는 기본적으로 final이다.
- 그래서 상속을 허용하려면 반드시 class 앞에 open을 붙여줘야 한다.

```kotlin
open class RichButton : Clickable { /* open이기 때문에 상속 가능 */

    fun disable() {} /* 이 함수는 final. 따라서 하위 클래스가 오버라이드 불가 */
    open fun animate() {} /* open이기 때문에 상속 가능 */
    final override fun click() {} /* override 메소드는 기본적으로 열려 있다. 닫기 위해선 final 앞에 붙여줘야 한다. */
}
```

- 코틀린에서도 abstract class 선언 가능하다.
- 추상 클래스에는 구현이 없는 추상 멤버가 있기 때문에 하위 클래스에서 그 추상 멤버를 오버라이드해야만 하는게 보통이다.
- 따라서 추상 멤버 앞에 open 변경자를 명시할 필요 없다.

```kotlin
abstract class Animated {
    abstract fun animate()
}
```

### 가시성 변경자: 기본적으로 공개
- 코틀린도 자바와 비슷하다.
- 코틀린의 기본 가시성은 public 이다.
- 자바에서는 기본 가시성이 패키지 전용(package-private) 이라고 한다. -> 처음 알았따...
- 근데 코틀린에서는 패키지를 네임스페이스를 관리하기 위한 용도로만 사용한다. -> 무슨 말인지 이해가 잘 안된다..
- 쨋든, 패키지 전용 가시성에 대한 대안으로 internal이라는 새로운 가시성 변경자를 제공 한다.
- 모듈 내부에서만 볼 수 있다는 뜻!! (모듈이란? 한꺼번에 컴파일되는 코틀린 파일을 의미 한다.)

| 변경자        | 클래스 맴버              | 최상위 선언             |
|------------|---------------------|--------------------|
| public(기본) | 모든 곳에서 볼 수 있다.      | 모든 곳에서 볼 수 있다.     |
| internal   | 같은 모듈 안에서만 볼 수 있다.  | 같은 모듈 안에서만 볼 수 있다. |
| protected  | 하위 클래스 안에서만 볼 수 있다. | 최상위 선언에 적용 불가      |
| private    | 같은 클래스 안에서만 볼 수 있다. | 같은 파일 안에서만 볼 수 있다. |

- 코틀린과 자바 가시성 규칙의 또 다른 차이점이 있음.
- 외부 클래스가 내부 클래스나 중첩된 클래스의 private 멤버에 접근 불가하다.

### 내부 클래스와 중첩된 클래스: 기본적으로 중첩 클래스
- 자바 처럼 클래스 안에 클래스 선언 가능
- 여기서 자바와의 차이는!! 중첩 클래스는 명시적으로 요청하지 않는 한 바깥쪽 클래스 인스턴스에 대한 접근 권한이 없다.
- 자바에서 중첩 클래스를 static으로 선언하면 그 클래스를 둘러싼 바깥쪽 클래스에 대한 묵시적인 참조가 사라진다.
- 하지만 코틀린에서는 아무런 변경자가 붙지 않으면 자동으로 static 선언이 된다.
- 만약 바깥쪽 클래스에 대한 참조를 포함하게 만들고 싶다면 inner 변경자를 붙이면 된다.

### 봉인된 클래스: 클래스 계층 정의 시 계층 확장 제한

```kotlin
interface Expr

class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

fun eval_v2(e: Expr): Int =
    if (e is Num) {
        e.value
    }else if (e is Sum) {
        eval_v2(e.right) + eval_v2(e.left)
    } else {
        throw IllegalArgumentException("Unknown expression")
    }
```
- 코틀린 컴파일러는 when을 사용해 Expr 타입의 값을 검사할 때 꼭 디폴트 분기인 else 분기를 덧붙이게 강제한다.
- 만약 실수로 새로운 클래스 처리를 잊어버린다면 else로 빠지게 되어 심각한 오류를 발생시킬 수도 있다.
- 코틀린에서는 이런 문제에 대한 해법을 제시한다.
- 클래스에 sealed 변경자를 붙이면 그 상위 클래스를 상속한 하위 클래스 정의를 제한할 수 있다.
- sealed 클래스의 하위 클래스를 정의 할 때는 반드시 상위 클래스 안에 중첩시켜야 한다.

```kotlin
sealed class Expr {
    class Num(val value: Int) : Expr()
    class Sum(val left: Expr, val right: Expr) : Expr()
}

fun eval_v2(e: Expr): Int = When(e) {
    is Expr.Num -> e.value
    is Expr.Sum -> eval_v2(e.right)+eval_v2(e.left)
}
```
- When 식에서 sealed 클래스의 모든 하위 클래스를 처리한다면 디폴트 분기가 필요 없다.
- sealed로 표시된 클래스는 자동으로 open임을 기억하자.
- 이렇게 하면 나중에 sealed 클래스를 상속 받는 하위 클래스를 만들면 when절에서 자동으로 컴파일 오류가 발생해 휴먼에러를 방지 할 수 있다.

### 뻔하지 않은 생성자와 프로퍼티를 갖는 클래스 선언
- 코틀린에서는 주 생성자와 부 생성자를 구분한다.

### 주 생성자
```kotlin
class User(
    val nickName: String,
)
```
- 이렇게 class 이름 뒤에 오는 괄호로 둘러싸인 코드를 주 생성자라고 부른다.
- 사실 User 뒤에 Constructor 생략되어 있음.
- 클래스에 기반 클래스가 있다면 주 생성자에서 기반 클래스의 생성자를 호출해야 할 필요가 있다.
- 기반 클래스를 초기화하려면 기반 클래스 이름 뒤에 괄호를 치고 생성자 인자를 넘긴다.

```kotlin
open class User(
    val nickName: String,
)

class TwitterUser(
    nickName: String
) : User(nickName) { /*...*/}
```
- 반면 인터페이스는 생성자가 없기 때문에 인터페이스 클래스 이름 뒤에는 괄호가 없다.
- 그래서 괄호가 있으면 기반 클래스, 없으면 인터페이스 클래스로 생각하면 된다.
- 만약 외부에서 클래스 생성을 막고 싶으면 private Constructor 붙여주면 된다.
```kotlin
class Secretive private constructor() {}
```

### 부 생성자: 상위 클래스를 다른 방식으로 초기화
- 코틀린에서는 생성자가 여럿 있는 경우가 자바보다는 훨씬 적다.
- 왜냐하면, 디폴트 파라미터를 사용하면 되니깐!!
- 근데 생성자가 여럿 필요한 경우가 가끔 있다.

```kotlin
open class View {
    constructor(ctx: Context) {

    }

    constructor(ctx: Context, attr: Attribute) {
        
    }
    
}
```
- 이 코드는 주 생성자는 없고 부 생성자만 있다.

```kotlin
class MyButton : View {
    constructor(ctx: Context) : super(ctx) {
        
    }
  
    constructor(ctx: Context, attr: AttributeSet) : super(ctx, attr) {
        
    } 
}
```
- 클래스를 확장한다면 똑같이 부 생성자를 정의할 수 있다.

### 인터페이스에 선언된 프로퍼티 구현
- 코틀린에서는 인터페이스에 추상 프로퍼티 선언을 넣을 수 있다.

```kotlin
interface User {
    val nickName: String
}
```
- 이는 User 인터페이스를 구현하는 클래스가 nickName의 값을 얻을 수 있는 방법을 제공해야 한다.
```kotlin
class PrivateUser(override val nickName: String) : User

class SubscribedUser(val email: String) : User {
    override val nickName: String
        get() = email.substringBefore('@')
}

class FacebookUser(val accountId: Int) : User {
    override val nickName = "Test"
}
```
- PrivateUser는 주 생성자 안에 프로퍼티를 직접 선언하는 간결한 구문을 사용
- SubscribedUser는 커스템 getter로 구현
- FacebookUser는 초기화 방식으로 구현
- 중요한 점은 SubscribedUser 구현 방식과 FacebookUser은 차이가 있다.
  - SubscribedUser는 get()으로 구현했기 때문에 호출 할 때 마다 email 값을 계산해서 돌려준다.
  - 반면 FacebookUser는 초기화 방식이기 때문에 처음에 지정했던 값을 불러와 돌려 준다.

### 게터와 세터에서 뒷받침하는 필드에 접근
- 값을 저장하는 동시에 로직을 실행할 수 있게 하기 위해서는 접근자 안에서 프로퍼티를 뒷받침하는 필드에 접근할 수 있어야 한다.

```kotlin
class User3(val name: String) {

    var address: String = "unspecified"
        set(value: String) {
            println("""
                Address was changed for $name:
                "$field" -> "$value".
            """.trimIndent())
            field = value
        }
}

fun main() {
    val user = User3("Lee")
    user.address = "테스트 주소입니다~! 323, 23123"
    println(user.address)
}
```
- 커스텀 세터를 정의해서 추가 로직을 실행한다.
- 접근자 본문에서는 field라는 특별한 식별자를 통해 뒷받침하는 필드에 접근 할 수 있다.
- 변경 가능 프로퍼티의 게터와 세터 중 한쪽만 직접 정의해도 된다는 점을 기억하자

### 접근자의 가시성 변경
- 접근자의 가시성을 변경 할 수 있다.
```kotlin
class LengthCounter() {

  var counter: Int = 0
    private set

  fun addWord(word: String) {
    counter += word.length
  }
}
```
- private set을 설정해서 값을 외부에서 변경 할 수 없게 했다.
- 대신 addWord 메서드를 제공해서 변경 할 수 있다.

### 컴파일러가 생성한 메소드: 데이터 클래스와 클래스 위임
- 자바 플랫폼에서는 equals, hashCode, toString 등의 메소드를 구현해야 한다.
- 자바에서는 ==를 원시 타입과 참조 타입을 비교할 때 사용한다.
  - 원시 타입의 경우 값이 같은지 비교한다. (동등성)
  - 참조 타입의 경우 주소 값이 같은지 비교한다.
  - 참조 타입의 경우 값을 비교하려면 equals()를 사용하면 된다.
- 하지만!! equals 메서드만 오버라이드 하면 제대로 동작하지 않는다.
- hashCode도 같이 재정의를 해줘야 정상적으로 동작한다.
  - JVM 언어에서는 hashCode가 지켜야 하는 equals()가 true를 반환하는 두 객체는 반드시 같은 hashCode()를 반환해야 한다라는 제약이 있기 때문이다.
  - 간단하게 생각하면, HashSet에서 비용을 줄이기 위해 가장 먼저 해쉬코드가 같은지 검사 한다.
  - 해쉬코드가 다르면 값 자체도 비교하지 않기 때문이다.
- 이런 메소드들은 IDE에서 자동으로 생성 해주긴 하지만 중복된 코드가 너무 많이 생긴다.
- 코틀린에서는 class 앞에 data를 붙이면 자동으로 생성해준다.

```kotlin
data class Client(
    val name: String,
    val postalCode: Int,
)
```
- 데이터 클래스의 프로퍼티가 꼭 val일 필요는 없다.
- 하지만 모든 프로퍼티를 읽기 전용으로 만들어서 데이터 클래스를 불면 클래스로 만들라고 권장한다.
- 다중스레드 환경에서 이런한 점은 매우 중요하다. 불면성이 보장되어야 다중스레드 환경에서 안전 하다는 것이기 때문에.

### 클래스 위임: by 키워드 사용
- 대규모 객체지향 시스템을 설계할 때 시스템을 취약하게 만드는 문제는 보통 구현 상속에 의해 발생한다.
- 종종 상속을 혀용하지 않는 클래스에 새로운 동작을 추가해야 할 때가 있다.
- 이럴 때 사용하는 일반적인 방법인 `데코레이터 패턴`이다.

```kotlin
class CountingSet<T>(
    val innerSet: MutableCollection<T> = HashSet<T>(),
) : MutableCollection<T> by innerSet {
    var objectsAdded = 0

    override fun add(element: T): Boolean {
        objectsAdded++
        return innerSet.add(element)
    }

    override fun addAll(c: Collection<T>): Boolean {
        objectsAdded += c.size
        return innerSet.addAll(c)
    }
}

fun main() {
    val cset = CountingSet<Int>()
    cset.addAll(listOf(1, 2, 3))
    println("${cset.objectsAdded} objects were added, ${cset.size} remain")
}
```

### Object 키워드: 클래스 선언과 인스턴스 생성
- 클래스를 정의 하면서 동시에 인스턴스를 생성한다는 공통점이 있다.
- 객체 선언: 싱글턴을 정의하는 방법 중 하나다.
- 동반 객체: 인스턴스 메소드는 아니지만 어떤 클래스와 관련 있는 메소드와 팩토리 메소드를 담을 때 쓰인다.

### 객체 선언: 싱글턴을 쉽게 만들기
- 객체지향 시스템을 설계하다 보면 인스턴스가 하나만 필요한 클래스가 유용한 경우가 많다.
```kotlin
object Payroll {
    val allEmployees = arrayListOf<String>()

    fun calculateSalary() {

    }
}
```
- 생성자는 객체 선언에 쓸 수 없다.
- 일반 클래스 인스턴스와 달리 싱글턴 객체는 객체 선언문이 있는 위치에서 생성자 호출 없이 즉시 만들어진다.
```kotlin
Payroll.allEmployees.add()
```
- 이렇게 바로 사용하면 된다.