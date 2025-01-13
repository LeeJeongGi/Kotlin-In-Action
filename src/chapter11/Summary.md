## DSL 만들기

### API에서 DSL로
- 우리의 궁극적인 목표는 코드의 가독성과 유지 보수성을 가장 좋게 유지하는 것이다.
- API가 깔끔하다는 말은 어떤 뜻일까?
  - 코드를 읽는 독자들이 어떤 일이 벌어질지 명확하게 이해할 수 있어야 한다.
  - 코드가 간결해야 한다.

### 영역 특화 언어라는 개념
- `범용 프로그래밍 언어`: 컴퓨터가 발명된 초기부터 컴퓨터로 풀 수 있는 모든 문제를 충분히 풀 수 있는 기능을 제공하는 것
- `영역 특화 언어`: 특정 과업 또는 영역에 초점을 맞추고 그 영역에 필요하지 않은 기능을 없앤 것
- DSL 대표적인 언어는 SQL 이다.
- DSL이 범용 프로그래밍 언어와 달리 더 선언적이라는 점이 중요하다.
- 명령적 언어는 어떤 연산을 완수하기 위해 필요한 각 단계를 순서대로 정확히 기술하는 것
- 선언적 언어는 원하는 결과를 기술하기만 하고 그 결과를 달성하기 위해 필요한 세부 실행은 언어를 해석하는 엔진에 맡긴다.
- 한 가지 단점으로는 애플리케이션과 함께 조합하기 어렵다.
- 그래서 이런 문제를 해결하면서 DSL 이점을 살리는 방법으로 내부 DSL이라는 개념이 유명해지고 있다.

### 내부 DSL
- 독립적인 문법 구조를 가진 외부 DSL과는 반대로 내부 DSL은 범용 언어로 작성된 프로그램의 일부이다.

```sql
SELECT Country.name, COUNT(Customer.id)
    FROM Country
    JOIN Customer
        ON Country.id = Customer.country_id
GROUP BY Country.name
ORDER BY COUNT(Cusstomer.id) DESC 
    LIMIT 1
```
- 외부 DSL로 작성된 언어이다.
- 코틀린의 내부 DSL을 사용한다면 이렇게 표현 할 수 있다.
- 
```kotlin
(country join Customer)
    .slice(Country.name, Count(Customer.id))
    .selectAll()
    .groupBy(Country.name)
    .orderBy(Count(Customer.id), isAsc = falce)
    .limit(1)
```
- 두 코드 모두 동일하게 실행된다.

### 수신 객체 지정 람다와 확장 함수 타입
- 수신 객체 지정 람다는 with, apply 같은 것들이다.

```kotlin
fun buildString(
    builderAction: StringBuilder.() -> Unit
): String {
    val sb = StringBuilder()
    sb.builderAction()
    return sb.toString()
}

fun main() {
    val s = buildString {
        append("Hello, ")
        append("World!")
    }
    println(s)
}
```
- 파라미터 타입을 선언할 때 확장 함수 타입을 사용했다.
- 확장 함수 타입 선언은 람다의 파라미터 목록에 있던 수신 객체 타입을 파라미터 목록을 여는 괄호 앞으로 빼 놓으면서 중간에 마침표(.)를 붙인 형태다.
- StringBuilder.() -> Unit: StringBuilder은 수신 객체 타입
- 또 다른 예로 String.(Int, Int) -> Unit
- 확장 함수의 본문에서는 확장 대상 클래스에 정의된 메소드를 마치 그 클래스 내부에서 호출하듯이 사용할 수 있다.

### invoke 관례: 함수처럼 호출할 수 있는 객체
- 관례는 특별한 이름이 붙은 함수를 일반 메소드 호출 구문으로 호출하지 않고 더 간단한 다른 구문으로 호출할 수 있게 지원하는 기능이다.
- operator 변경자가 붙은 invoke 메소드 정의가 들어있는 클래스의 객체를 함수처럼 호출할 수 있다.

```kotlin
class Greeter(
    val greeting: String
) {
    operator fun invoke(name: String) {
        println("$greeting, $name")
    }
}

fun main() {
    val bavarianGreeter = Greeter("bavarian")
    bavarianGreeter("Dmitry")
}
```

```text
>> bavarian, Dmitry
```
- Greeter 안에 invoke 메소드를 정의한다.
- 따라서 Greeter 인스턴스를 함수 처럼 호출할 수 있다.
- bavarianGreeter("Dmitry") -> bavarianGreeter.invoke("Dmitry"): 내부적으로 이렇게 컴파일 된다.

### invoke 관례와 함수형 타입
- 일반적인 람다 호출 방식이 실제로는 invoke 관례를 적용한 것에 지나지 않는다.
- 인라인하는 람다를 제외한 모든 람다는 함수형 인터페이스를 구현하는 클래스로 컴파일된다.
- 각 함수형 인터페이스 이름이 가리키는 개수만큼 파라미터를 받는 invoke 메소드가 들어있다.

```kotlin
interface Function2<in P1, in P2, out R> {
  operator fun invoke(p1: P1, p2: P2): R
}
```
- 람다를 함수처럼 호출하면 이 관례에 따라 invoke 메소드 호출로 변환된다.

```kotlin
package chapter11

data class Issue(
    val id: String,
    val project: String,
    val type: String,
    val priority: String,
    val description: String,
)

class ImportantIssuePredicate(val project: String): (Issue) -> Boolean {

    override fun invoke(issue: Issue): Boolean {
        return issue.project == project && issue.isImportant()
    }

    private fun Issue.isImportant(): Boolean {
        return type == "Bug" && (priority == "Major" || priority == "Critical")
    }
}

fun main() {
    val i1 = Issue("IDEA-154446", "IDEA", "Bug", "Major", "Save settings failed")
    val i2 = Issue("KT-12183", "Kotlin", "Feature", "Normal", "intention: convert ..")

    val predicate = ImportantIssuePredicate("IDEA")
    for (issue in listOf(i1, i2).filter(predicate)) {
        println(issue.id)
    }
}
```
- 술어의 로직이 복잡해서 한 람다로 표현하기 어렵다.
- 그래서 람다를 여러 메소드로 나누고 각 메소드에 뜻을 명확히 알 수 있는 이름을 붙이고 싶다.
- 람다를 함수 타입 인터페이스를 구현하는 클래스로 변환하고 그 클래스의 invoke 메소드를 오버라이드하면 그런 리펙토링이 가능하다.
