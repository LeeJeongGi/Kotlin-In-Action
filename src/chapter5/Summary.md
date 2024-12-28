## 람다로 프로그래밍
- 람다는 기본적으로 다른 함수에 넘길 수 있는 작은 코드 조각을 뜻함
- 람다를 자주 사용하는 경우로 컬렉션 처리를 들 수 있다.

### 람다 소개: 코드 블록을 함수 인자로 넘기기
- 이벤트가 발생하면 이 핸들러를 실행하자 또는 데이터 구조의 모든 원소에 이 연산을 적용하자와 같은 생각을 코드로 표현하기 위해 일련의 동작을 변수에 저장하거나 다른 함수에 넘겨야 하는 경우가 자주 있다.

```java
button.setOnClickListener(new OnclickListener () {
    @override
    public void onClick(View view) {
        /* 클릭 시 수행할 동작 */
    }
})
```
- 자바에서는 이렇게 무명 내부 클래스로 사용했다.
- 하지만 번잡스럽기 때문에 람다를 사용한다.

```kotlin
button.setOnClickListener {/* 클릭 시 수행할 동작 */}
```
- 같은 기능을 하는 코드인데 람다가 훨씬 깔끔하다.

### 람다와 컬렉션

```kotlin
data class Person(
    val name: String,
    val age: Int,
)

fun main() {
    val persons = listOf(Person("Alice", 29), Person("Bob", 31), Person("Carol", 33))
    println(persons.maxBy { it.age })
}
```
- 모든 컬렉션에 대해 maxBy 함수를 호출할 수 있다.
- { it.age } 는 바로 바교에 사용할 값을 돌려주는 함수이다.
- 람다식을 사용하지 않는다면 for 문을 돌면서 나이가 가장 많은 사람을 찾는 번거로운 코드를 작성해야 한다.

```kotlin
persons.maxBy(Person::age)
```
- 이렇게 멤버 참조를 사용하여 표현 할 수도 있다.

### 람다식의 문법

```text
{ x: Int, y: Int -> x + y }
|----파라미터-----|   |-본문-|
```

```kotlin
val sum = {x: Int, y: Int -> x + y}
println(sum(1,2))
```
- 람다식을 변수에 저장할 수 있다.
- 람다가 저장된 변수를 다른 일반 함수와 마찬가지로 다룰 수 있다.
- `실행시점에 코틀린 람다 호출에는 아무 부가 비용이 들지 않는다.`

```kotlin
val persons = listOf(Person("Alice", 29), Person("Bob", 31), Person("Carol", 33))

println(persons.maxBy { it.age })
println(persons.maxBy{ p: Person -> p.age})
println(persons.maxBy(Person::age))
```
- {} 안에 있는 식은 람다식이다.
- maxBy 함수에 람다식을 넘긴다.
- 람다 식은 Person 타입의 값을 인자로 받아서 인자의 age를 반환한다.
- 로컬 변수처럼 텀파일러는 람다 파라미터의 타입도 추론할 수 있다.

```kotlin
val sum2 = { x: Int, y: Int ->
    println("Computing the sum of $x and $y...")
    x + y
}
println(sum2(1, 2))
```
- 한 줄이 아닌 여러줄로 이루어진 람다식도 있다.
- 맨 마지막에 있는 식이 람다의 결과 값이 된다.

### 현재 영역에 있는 변수에 접근
- 람다를 함수 안에서 정의하면 함수의 파라미터뿐 아니라 람다 정의의 앞에 선언된 로컬 변수까지 람다에서 모두 사용할 수 있다.

```kotlin
fun printMessageWithPrefix(message: Collection<String>, prefix: String) {
    message.forEach {
        println("$prefix: $it")
    }
}

fun printProblemCounts(responses: Collection<String>) {
    var clientErrors = 0
    var serverErrors = 0

    responses.forEach { a ->
        if (a.startsWith("4")) {
            clientErrors++
        } else if (a.startsWith("5")) {
            serverErrors++
        }
    }

    println("$clientErrors client errors, $serverErrors server errors")
}

fun main() {
    val msg = listOf("403 Forbidden", "404 Not Found", "500 Server Error")
    println(printMessageWithPrefix(msg, prefix = "Error :"))

    printProblemCounts(msg)
}
```
- forEach 는 컬렉션 조작 함수 중 하나이다.
- 컬렉션의 모든 원소에 대해 람다를 호출해준다.
- 변수에 접근 가능 
- clientErrors, serverErrors와 같이 람다 안에서 사용하는 외부 변수를 람다가 포획한 변수라고 부른다.
- 함수가 반환되면 로컬 변수는 반환된다.

### 멤버 참조
- 람다를 사용해 코드 블록을 다른 함수에게 인자로 넘기는 방법을 배웠다.
- 근데 넘기려는 코드가 이미 함수로 선언된 경우는 어떻게 해야 할까?
- 다시 작성할 수도 있지만 그건 중복이다.
- 그래서!! ::(이중 클론) 사용을 한다.

```text
Person::age -> Class::멤버
```
```kotlin
persons.maxBy { it.age }
persons.maxBy(Person::age)
```
- 이렇게 호출해서 사용할 수 있다.

### 컬렉션 함수형 API
- 함수형 프로그래밍 스타일을 사용하면 컬렉션을 다룰 때 편리하다.
- 코드를 아주 간결하게 만들 수 있음.

### 필수적인 함수: filter와 map
```kotlin
fun main() {
    val numbers = listOf(1, 2, 3, 4, 5, 6, 7)
    println(numbers.filter { it % 2 == 0})
    println(numbers.map { it * it})
}
```
- filter 함수는 컬렉션을 이터레이션하면서 주어진 람다에 각 원소를 넘긴다.
- filter 함수는 원치 않는 원소를 제거한다.
- 하지만 filter는 원소를 변환 할 수는 없다.
- 그래서 map을 사용해서 변환해야 한다.
- map 함수는 주어진 람다를 컬렉션의 각 원소에 적용한 결과를 모아서 새 컬렉션을 만든다.

```kotlin
val persons = listOf(Person("Alice", 29), Person("Bob", 31), Person("Carol", 33))
persons.map {it.name}
```
- name 의 리스트가 출력된다.

```kotlin
people.filter {it.age == people.maxBy{Person::name}!!.age}
```
- 여기서 가장 나이 많은 사람의 이름을 알고 싶을 때 사용하는 함수
- 표현은 간단하지만 목록에서 최댓값을 구하는 작업을 계속 반복한다는 단점이 있다.
```kotlin
val maxAge = people.maxBy{Person::name}
people.filter {it.age == maxAge}
```
- 이렇게 수정하는게 더 합리적이다.

### all, any, count, find: 컬렉션에 술어 적용
```kotlin
val cntBeInClub27 = { p: Person -> p.age <= 27 }
val people = listOf(Person("Alice", 27), Person("Bob", 31))
println(people.all(canBeInClub27)) // false
println(people.any(canBeInClub27)) // true
println(people.count(canBeInClub27)) // 1
println(people.find(canBeInClub27)) // Person(name="Alice", age=27)
```
- 모든 원소가 cntBeInClub27를 만족하는지 보고 싶다면 all 사용하면 된다.
- 원소가 하나라도 있는지 궁금하면 any를 쓴다.
- 만약 만족하는 원소 갯수를 구하고 싶으면 count를 사용하면 된다.
- 술어를 만족하는 원소를 하나 찾고 싶으면 find 함수를 사용하면 된다.

### groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경
```kotlin
val persons2 = listOf(Person("Alice", 29), Person("Bob", 31), Person("Carol", 31))
val temp = persons2.groupBy { it.age }
```
```text
29=Person("Alice", 29)
31={Person("Bob", 31), Person("Carol", 31)}
```
- 위와 같은 형식으로 저장 된다. Map<Int, List<Person>>

### flatMap과 flatten: 중첩된 컬렉션 안의 원소 처리
```kotlin
class Book(
    val title: String,
    val authors: List<String>,
)

fun main() {
    val strings = listOf("abc", "def")
    println(strings.flatMap { it.toList() })

    val books = listOf(
        Book("Thursday Next", listOf("Jasper Fforde")),
        Book("Mort", listOf("Terry Pratchett")),
        Book("Good Morning", listOf("Terry Pratchett", "Neil GaiMan")),
    )
    println(books.flatMap { it.authors }.toSet())
}
```
```text
호출 결과
[a, b, c, d, e, f]
[Jasper Fforde, Terry Pratchett, Neil GaiMan]
```
- flatMap 함수는 먼저 인자로 주어진 람다를 컬렉션의 모든 객체에 적용하고 람다를 적용한 결과 얻어지는 여러 리스트를 한 리스트로 한데 모은다.

### 지연 계산 컬렉션 연산
- 앞에서 살펴본 map, filter 같은 함수는 결과 컬렉션을 즉시 생성한다.
- 이는 컬렉션 함수를 연쇄하면 매 단계마다 계산 중간 결과를 새로운 컬렉션에 임시로 담는다는 말이다.
- 시퀀스를 사용하면 중간 임시 컬렉션을 사용하지 않고도 컬렉션 연산을 연쇄할 수 있다.

```kotlin
fun main() {
    listOf(1, 2, 3, 4).asSequence()
        .map { print("map($it) "); it * it }
        .filter { print("filter($it) "); it % 2 == 0 }
        .toList()

    val number = listOf(1, 2, 3, 4).map { it * it }.find { it > 3 }
    println(number)

}
```
- Sequnece 인터페이스 안에는 iterator라는 단 하나의 메소드가 있다.
- 시퀀스를 다시 컬렉션으로 마지막에 돌려야 한다.
  - 그냥 써도 상관없지만 다른 api 메소드가 필요하다면 시퀀스를 리스트로 변환해야 한다.

### 시퀀스 연산 실행: 중간 연산과 최종 연산
```kotlin
listOf(1, 2, 3, 4).asSequence()
        .map { print("map($it) "); it * it }
        .filter { print("filter($it) "); it % 2 == 0 }
```
- 이 코드를 실행하면 아무 내용도 출력되지 않는다.
- 이는 map, filter 변환이 늦춰져서 결과를 얻을 필요가 있을 때 적용된다는 뜻이다.
 ```kotlin
listOf(1, 2, 3, 4).asSequence()
        .map { print("map($it) "); it * it }
        .filter { print("filter($it) "); it % 2 == 0 }
        .toList()
```
- toList()라는 최종 연산을 호출해야 출력 된다.
- 시퀀스의 경우 모든 연산은 각 원소에 대해 순차적으로 적용된다!!!
- 즉 첫 번째 원소가 처리되고, 다시 두 번째 원소가 처리된다.
- 반면 map, filter는 원소 묶음 단위로 처리된다.

```kotlin
println(listOf(1,2,3,4).asSequence()
    .map { it * it }.find { it > 3 })
```
- 만약 같은 결과를 시퀀스가 아니라 컬렉션에 수행하면 map의 결과가 먼저 평가 된다.
  - {2, 4, 6, 8} 이렇게 임시 컬렉션에 담긴다.
- 그 다음 find 메서드를 통해 처리 된 후에 {4, 6, 8} 이 반환된다.
- 그런데 시퀀스를 사용하는 경우에는 원소 마다 처리되기 때문에 방식이 조금 다르다.
  - 1 -> map (1) -> find (it > 3) 처리
  - 2 -> map (4) -> find (it > 3) 처리 >> 반환
  - 3 -> map (9) -> find (it > 3) 처리 >> 종료되어 작업 안함.
  - 4 -> map (16) -> find (it > 3) 처리 >> 종료되어 작업 안함.
- 각각 원소마다 실행 되기 때문에 find 해서 원소 4를 반환하고 뒤에 3, 4 에 대해서는 더 이상 진행하지 않는다.

### 자바 함수형 인터페이스 활용

```java
public interface OnClickListener {
  void onClick(View v);
}

public class Button {
  public void setOnClickListener(OnClickListener l) { /**/ }
}

public static void main(String[] args) {
  Button button = new Button();
  button.setOnClickListener(new OnClickListener() {
    @Override
    public void onClick(View v) {
        /* 무명 클래스 작성 */
    }
  });
}
```
- 기존 자바에서는 이렇게 무명 클래스를 넘겨야 했다.
- 코틀린에서는 무명 클래스 인스턴스 대신 람다를 넘길 수 있다.
```kotlin
button.setOnClickListener { view -> ...}
```
- 이런 코드가 동작하는 이유는 OnClickListener에 추상 메서드가 단 하나만 있기 때문이다.
- 그런 인터페이스를 `함수형 인터페이스`라고 한다.
- 코틀린은 함수형 인터페이스를 인자로 취하는 자바 메서드를 호출할 때 람다를 넘길 수 있게 해준다.

### 자바 메소드에 람다를 인자로 전달
- 함수형 인터페이스를 인자로 원하는 자바 메소드에 코틀린 람다를 전달할 수 있다.

```java
void postponeComputation(int delay, Runnable computation);
```
- 코틀린에서 람다를 이 함수에 넘길 수 있다.
```kotlin
postponeComputation(1000) {println(42)}
```
- 컴파일러는 자동으로 람다({} 안에 식)를 Runnable 인스턴스로 변환해준다.
- 람다와 무명 객체 사이에는 차이가 있음.
  - 객체를 명시적으로 선언하는 경우 메소드를 호출할 때마다 새로운 객체가 생성된다.
  - 흠,,, 함수형 인터페이스를 받는 자바 메소드를 코틀린에서 호출할 때 쓰는 방식이라..어찌고..
  - 그래서 코틀린에서는 inline으로 표시된 코틀린 함수에게 람다를 넘기면 아무런 무명 클래스도 만들어지지 않는다! 결론..

### 수신 객체 지정 람다: with와 apply
```kotlin
fun alphabet(): String {
    val result = StringBuilder()
    for (letter in 'A'..'Z') {
        result.append(letter)
    }

    result.append("\nNow I know the alphabet!")
    return result.toString()
}
```
- with를 사용하기 전에 일반적으로 for 문을 사용해서 알파벳을 만드는 구문이다.
- 코드가 우선 상당히 길다.
```kotlin
fun alphabet2(): String {
    val result = StringBuilder()
    return with(result) { // 수신 객체를 지정
        for (letter in 'A'..'Z') {
            this.append(letter) // this를 명시해서 앞에서 지정한 수신 객체의 메소드를 호출한다.
        }
        append("\nNow I know the alphabet!")
        this.toString()
    }
}
```
- with문은 언어가 제공하는 특별한 구문처럼 보인다.
- 하지만 실제로는 파라미터가 2개 있는 함수다.
- 첫 번째 파라미터는 StringBuilder이고 두 번째 파라미터는 람다다.

```kotlin
fun alphabet3(): String {
    return with(StringBuilder()) {
        for (letter in 'A'..'Z') {
            append(letter)
        }
        append("\nNow I know the alphabet!")
        toString()
    }
}
```
- StringBuilder 변수를 없애면 더 깔끔하다.

### apply 함수
- 거의 with와 동일하다.
- 유일한 차이는 apply는 항상 자신에게 전달된 객체를 반환한다는 점뿐이다.

```kotlin
fun alphabet() = StringBuilder().apply {
    for (letter in 'A'..'Z') {
        append(letter)
    }
    append("\\nNow I know the alphabet!")
}.toString()
```
- StringBuilder에 정의된 확장 함수라고 생각하자.
- apply의 수신 객체가 전달받은 람다의 수신 객체가 된다.
- apply 함수는 객체의 인스턴스를 만들면서 즉시 프로퍼티 중 일부를 초기화해야 하는 경우 유용하다.
