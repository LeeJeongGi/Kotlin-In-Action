## 코틀린 기초

### 기초 문법
- 함수를 선언할 때 fun 키워드를 사용한다.
- 파라미터 이름 뒤에 그 파라미터의 타입을 쓴다.
- 함수를 최상위 수준에 정의할 수 있다. 꼭 클래스 안에 함수를 넣어야 할 필요가 없다.
- 문장 뒤에 세미클론 생략 가능

```text
문(statement)과 식(expression)의 구분

코틀린에서 if는 식이지 문이 아니다.

식은 값을 만들어 내며 다른 식의 하위 요소로 계산에 참여 할 수 있는 반면 문은 자신을 둘러싸고 있는
가장 안쪽 블록의 최상위 요소로 존재하며 아무런 값을 만들어내지 않는다는 차이가 있다.

자바에서는 모든 제어 구조가 문인 반면 코틀린에서는 루프를 제외한 대부분의 제어 구조가 식이다.
```

```kotlin
/**
 * 블록이 본문인 함수
 */
fun max1(a: Int, b: Int): Int {
    return if (a > b) a else b
}

/**
 * 식이 본문인 함수, 반환 타입인 Int 생략 가능
 */
fun max2(a: Int, b: Int): Int = if (a > b) a else b
```

- 코틀린에서는 식이 본문인 함수가 자주 쓰인다고 함.
- 반환 타입을 생략 가능한 이유는 ?
  - 컴파일러가 알아서 추론 해준다. (타입 추론)
- 블록이 본문인 함수에서는 반환 타입을 항상 명시해 줘야 한다.
  - 블록안에 여러개의 return문이 있는 경우가 많다.

### 변수
- val: 변경 불가능한 참조를 저장하는 변수
- var: 변경 가능한 참조

`권장하는 방법`
- 기본적으로 모든 변수를 val 키워드를 사용한다.
- 꼭 필요할 때에만 var로 변경해서 사용해라!!
- why ? 변경 불가능한 참조와 변경 불가능한 객체를 부수 효과가 없는 함수와 조합해 사용하면 코드가 함수형 코드에 가까워진다.
  - 함수형 코드 장점은 1장에서 설명했음.

```text
val 참조 자체는 불면일지라도 그 참조가 가리키는 객체의 내부 값은 변경될 수 있다는 사실을 기억해라!!

가장 많이 착각하는 유형(바로 나)
val languages = arrayOf("Java")
languages.add("kotlin") <-- 가능
```

```kotlin
var answer = 31
answer = "answer"
```

- 이렇게 사용 못한다. 왜냐하면 컴파일러는 변수 선언 시점의 초기화 식으롭부터 변수의 타입을 추론한다.
- 그래서 answer = 31 에서 컴파일러는 answer 타입을 Int로 생각한다.
- 결론, 위 처럼 쓰면 안된다. 에러 발생

### 문자열 템플릿

```kotlin
fun main(args: Array<String>) {
    val name = if (args.size > 0) args[0] else null
    println("Hello, $name!")
}
```
- 코틀린에서도 문자열 안에 별수를 사용할 수 있다.
- $를 문자 앞에 추가해서 사용하면 된다.
- 저 문장은 결국 "Hello" + name + "!" 이거와 동일한 문장

### 클래스와 프로퍼티

- 자바 버전
```Java
public class Person_j {
    private final String name;

    public Person_j(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

- 코틀린 버전
```kotlin
class Person(
    val name: String,
    var isMarried: Boolean
)
```

- 만약 자바 코드에서 name 말고 새로운 필드가 추가 된다면?
  - 생성자 코드 추가, Getter, Setter 추가.. 반복적인 무의미한 코드 증가!!
- 자바에서는 필드와 접근자(Getter, Setter)를 한데 묶어 `프로퍼티`라고 부른다.
- 반면, 코틀린에서는 혁명이 일어난다.
  - val name: String 자체가 필드와 필드를 읽는 단순한 게터를 자동으로 만들어 낸다.
  - var name: String 이라면 세터까지 자동으로 만들어 준다.

```Java
Person person = new Person("Lee");
System.out.println(person.getName());
System.out.println(person.isMarried());
```

```kotlin
val person = Person("Lee", true)
System.out.println(person.name)
System.out.println(person.isMarried)
```

- 동일한 결과를 가져오지만 가독성과 간결함 모두 코틀린으로 작성했을 때 좋아졌다.
- 원한다면 프로퍼티 값을 그때그때 계산 할 수도 있다.
- 바로 커스템 게터를 사용하면 된다.

```kotlin
class Rectangle(
    val height: Int,
    val width: Int,
) {
    val isSquare: Boolean
        get() {
            return height == width
        }
}
```

- Rectangle 생성 될 때마다 커스텀 Getter에서 값을 비교 후 필드 값을 만들어 준다.
- 물론 자바에서도 메서드를 만들어서 구현 할 수도 있다.
- 근데 가독성 측면에서 코틀린이 정말 우월하다고 나는 생각한다.

### 선택 표현과 처리: enum과 when

- enum class
```kotlin
enum class Color(
    var r: Int,
    var g: Int,
    var b: Int,
) {
    RED(255, 0, 0),
    GREEN(0, 255, 0),
    BLUE(1,2,3),
    YELLOW(4,5,6); /*마지막은 반드시 세미클론을 붙여줘야 한다!!!!*/

    fun rgb(): Int {
        return (r * 256 + g) * 256 + b
    }
}

fun main() {
    println(Color.BLUE.rgb())
}
```
- 코틀린에서 enum은 소프트 키워드라 부르는 존재다.
- class 앞에 있을 때는 특별한 의미를 가지지만 다른 곳에서는 사용할 수 없다.

```kotlin
fun getMnemonic(color: Color) {
    when (color) {
        Color.RED -> "Richard"
        Color.GREEN -> "Gold"
        Color.BLUE -> "Blue"
        Color.YELLOW -> "Yellow"
    }
}

fun getWarmth(color: Color) = when (color) {
    Color.RED, Color.GREEN -> "worm"
    Color.BLUE, Color.YELLOW -> "cold"
}

fun mix(c1: Color, c2: Color) = when(setOf(c1, c2)) {
    setOf(Color.RED, Color.YELLOW) -> Color.RED
    setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN
    setOf(Color.BLUE, Color.RED, Color.YELLOW) -> Color.BLUE
    else -> {
        throw Exception("Dirty color")
    }
}

fun mixOptimized(c1: Color, c2: Color) = when {
    (c1 == Color.RED && c2 == Color.YELLOW) || (c1 == Color.BLUE && c2 == Color.YELLOW) -> {
        Color.RED
    }
    else -> {
        throw Exception("Dirty color")
    }
}
```

- when의 다양한 사용 방법이 있다.
- 사용 방법은 위의 코드를 확인 하면 된다.
- 코드 작성하고 보니 가독성이 정말 직관적이라 읽기가 너무 쉽다.
- 자바에 비하면 확실히 switch문은 차이가 있다고 생각한다.

- 타입 캐스트
```kotlin
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
```
- 자바와 다른 점은 자바는 if문으로 타입을 검사한 후에 타입으로 변환 후 사용한다.
- 반면 코틀린에서는 타입 검사와 변환이 한번에 진행된다.

- for 문
```kotlin
fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "FizzBuzz "
    i % 3 == 0 -> "Fizz"
    i % 5 == 0 -> "Buzz"
    else -> "$i "
}

fun main() {
    for (i in 1..100) {
        println(fizzBuzz(i))
    }

    for (i in 100 downTo 1 step 2) {
        println(fizzBuzz(i))
    }
}
```
- 1..100 처럼 range를 설정하면 된다. (1 <= i <= 100 같은 의미)
- step 옵션을 줘서 건너 뛰면서 검사 할 수도 있다.
- 예제 코드 작성 하면서 가장 좋다고 느낀 부분은 향상된 for문이다.

```kotlin
fun main() {
    for (c in 'A'..'Z') {
        val binary = Integer.toBinaryString(c.toInt())
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }

    val list = listOf("10", "11", "12")
    for ((index, element) in list.withIndex()) {
        println("$index: $element")
    }

    println(isLetter('d'))
    println(isDigit('3'))
    println(recognize(c = 'e'))
    println(recognize(c = '3'))
}

fun isLetter(c: Char) = c in 'a'..'z' || c in 'A'..'Z'
fun isDigit(c: Char) = c in '0'..'9'

fun recognize(c: Char) = when (c) {
    in '0'..'9' -> "it's a digit!!!!"
    in 'a'..'z' -> "it's a letter!!!!"
    else -> "i don't know"
}
```

- for ((letter, binary) in binaryReps) 선언을 보면 뭘까?
  - letter, binary에 각각 key와 value를 할당할 수 있다.
  - 자바였다면 get(key)로 작성해야 하는데 한번에 선언해서 사용 가능하다!!
- for ((index, element) in list.withIndex()) List도 마찬가지!
  - index와 value를 한번에 가져 올 수 있다..
- in 키워드로 범위를 검사 할 수도 있다.

### 예외 처리

```kotlin
fun readNumber(reader: BufferedReader) {
    val number = try {
        val line = reader.readLine()
        Integer.parseInt(line)
    } catch (e: NumberFormatException) {
        null
    }

    println(number)
}
```

- 기본적으로 예외 메커니즘은 자바와 동일하다.
  - checked Exception: 예외를 잡던지 위로 던져야 한다.
  - unchecked Exception: 따로 어떻게 처리할 지 선언해 줘야 한다.
- try-catch 역시 val 변수에 받을 수 있다
- 자바와 가장 큰 차이점은 자바에서는 체크 예외를 명시적으로 처리해야 한다.
- 하지만 코틀린에서는 체크 예외, 언체크 예외 구별하지 않는다.
  - 자바에서는 체크 예외 처리를 강제하기 때문에 의미 없이 예외를 다시 던지거나, 예외를 잡되 처리하지는 않고 무시하는 코드를 작성하는 경우가 흔하다.














