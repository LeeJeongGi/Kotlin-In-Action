## 애노테이션과 리플렉션

### 애노테이션 적용
- 코틀린에서는 자바와 같은 방식으로 애노테이션을 사용할 수 있다.
- 흥미로운 예제로 @Deprecated 애노테이션이 있다.
  - API 사용자는 그 패턴을 보고 지원이 종료될 API 기능을 더 쉽게 새 버전으로 포팅할 수 있다.

```kotlin
@Deprecated("Use removeAt(index) instead.", ReplaceWith("removeAt(index"))
fun remove(index: Int) {/*...*/}
```
- remove 말고 removeAt 사용하세요~ 라는 의미이다.

### 애노테이션 대상
- `사용 지점 대상` 선언으로 애노태이션을 붙일 요소를 정할 수 있다.
- @ 기호와 애노테이션 이름 사이에 붙으며, 애노테이션 이름과는 클론(:)으로 분리된다.
  - @get : Rule
  - @get은 사용 지점 대상
  - Rule 어노테이션 이름

### 애노테이션을 활용한 JSON 직렬화 제어
- `직렬화`는 객체를 저장장치에 저장하거나 네트워크를 통해 전송하기 위해 텍스트나 이진 형식으로 변환하는 것이다.
- `역직렬화`는 텍스트나 이진 형식으로 저장된 데이터로부터 원래의 객체를 만들어 낸다.
- @JsonExclude: 애노테이션을 사용하면 직렬화나 역직렬화 시 프로퍼티를 무시할 수 있다.
  - 직렬화 대상에서 제외할 프로퍼티에는 반드시 디폴트 값을 지정해야 한다.
- @JsonName: 애노테이션을 사용하면 프로퍼티를 표현하는 키/값 쌍의 키로 프로퍼티 이름 대신 애노테이션이 지정한 이름을 쓰게 할 수 있다.

```kotlin
data class Person(
    @JsonName("alias")
    val firstName: String,
    
    @JsonExclude
    val age: Int? = null,
)
```

```text
직렬화 : Person("Alice") <-> {"alias": "Alice"} :역직렬화 
```

### 애노테이션 선언

```kotlin
annotation class JsonExclude
```
- @JsonExclude 애노테이션은 아무 파라미터도 없는 가장 단순한 애노테이션이다.
- 애노테이션 클래스는 오직 선언이나 식과 관련 있는 메타데이터 구조를 정의하기 때문에 내부에 아무 코드도 들어있을 수 없다.
- 파라미터가 있는 애노테이션을 정의하려면 애노테이션 클래스의 주 생성자에 파라미터를 선언해야 한다.

```kotlin
annotation class JsonName(val name: String)
```

### 메타애노테이션: 애노테이션을 처리하는 방법 제어
- 애노테이션 클래스에 적용할 수 있는 애노테이션을 메타애노테이션이라고 부른다.
- 가장 흔히 쓰이는 메타애노테이션은 @Target 이다.

```kotlin
@Target(AnnotationTarget.PROPERTY)
annotation class JsonExclude
```
- @Target 메타애노테이션은 애노테이션을 적용할 수 있는 요소의 유형을 지정한다.
- @Target을 지정하지 않으면 모든 선언에 적용할 수 있는 애노테이션이 된다.

### 애노테이션 파라미터로 클래스 사용

```kotlin
interface Company {
    val name: String
}

data class CompanyImpl(
    override val name: String,
): Company

data class Person(
    val name: String,
    @DeserializeInterface(CompnayImpl::Class)
    val company: Company
)
```
- 직렬화된 Person 인스턴스를 역직렬화하는 과정에서 company 프로퍼티를 표현하는 JSON을 읽으면 그 프로퍼티에 값에 해당하는 JSON을 역직렬화하면서 CompanyImpl 인스턴스를 만들어서 대입한다.

```kotlin
annotation class DeserializeInterface(
    val targetClass: KClass<out Any>
)
```
- KClass는 자바 java.lang.Class 타입과 같은 역할을 하는 코틀린 타입이다.

### 애노테이션 파라미터로 제네릭 클래스 받기
- 직렬화하는 기본 기능을 바꾸고 싶으면 직접 제공하면 된다.

```kotlin
interface ValueSerializer<T> {
  fun toJsonValue(value: T) : Any?
  fun fromJsonValue(jsonValue: Any?): T
}
```
- 이 때 날짜를 직렬화 한다고 가정해보자.

```kotlin
data class Person(
    val name: String,
    @CustomSerializer(DateSerializer::Class)
    val birthDate: Date
)

annotation class CustomSerializer(
    val serializerClass: KClass<out ValueSerializer<*>>
)
```
- CustomSerializer가 ValueSerializer 구현하는 클래스만 인자로 받아야 함을 명시한다.
- 따라서 제네릭 클래스를 인자로 받아야 하면 KClass<out 허용할 클래스 이름<*>>

### 리플렉션: 실행 시점에 코틀린 객체 내부 관찰
- 리플렉션은 실행 시점에 객체의 프로퍼티와 메소드에 접근할 수 있게 해주는 방법이다.
- KClass를 사용하면 클래스 안에 있는 모든 선언을 열거하고 각 선언에 접근하거나 클래스의 상위 클래스를 얻는 등의 작업이 가능하다.

### 리플렉션을 사용한 객체 직렬화 구현

```kotlin
fun serialize(obj: Any): String
```
- 이 함수는 객체를 받아서 그 객체에 대한 JSON 표현을 문자열로 돌려준다.
- StringBuilder 객체 뒤에 문자열을 추가하는데 작업을 편리하게 하기 위해 StringBuilder 확장 함수로 구현한다.

```kotlin
fun serialize(obj: Any): String = buildString {serializeObjec(obj)}
```

```kotlin
fun StringBuilder.serializeObject(obj: Any) { 
    val kClass = obj.javaClass.kotlin
    val properties = kClass.memberProperties
    properties.joinToStringBuilder(
        this, prefix = "{", postFix = "}" { prop -> 
            serializeString(prop.name)
            append(":")
            serializePropertyValue(prop.get(obj))
      }
    )
}
```
- 결과로 {prop1: value1, prop2: value2} .. 이런식으로 나온다.
- serializeString 함수는 JSON 명세에 따라 특수 문자를 이스케이프 해준다.
  - 이스케이프란 특수 문자를 의미를 가진게 아닌 그냥 문자로 인식하게 변경해준다.

### 애노테이션을 활용한 직렬화 제어
- KAnnotatedElement 인터페이스에는 annotations 프로퍼티가 있다.
- 그래서 @JSonExclude 애노테이션이 붙어 있으면 포함 안시킬 수 있다.

```kotlin
val properties = kClass.memberProperties
  .filter(it.findAnnotation<JsonExclude>() == null)
```
- JsonExclude 애노테이션이 붙은 필드를 제어할 수 있다.
- @JsonName도 마찬가지로 annotations 메서드를 통해 변경할 수 있다.

```kotlin
fun StringBuilder.serializeProperty(
    prop: KProperty1<Any, *>,
    ojb: Any
) {
    val jsonNameAnn = prop.findAnnotation<JsonName>()
    val propName = jsonNameAnn?.name ?: prop.name
    serializeString(propName)
    append(":")
    serializePropertyValue(prop.get(obj))
}
```
- val jsonNameAnn = prop.findAnnotation<JsonName>(), 
- val propName = jsonNameAnn?.name ?: prop.name
- JsonName 애노테이션이 붙어 있으면 @JsonName 으로 설정된 이름을 아니면 prop.name 을 반환하는걸 보면 된다.

### JSON 파싱과 객체 역직렬화
- 역직렬화는 직렬화 과정보다 복잡하다.
- 첫 단계는 어휘 분석기로 렉서라고 부른다.
- 두 번째 단계는 문법 분석기로 파서라고 부른다.
- 마지막 단계는 파싱한 결과로 객체를 생성하는 역직렬화 컴포넌트다.

```text
"{"title" : "Catch-22", "author" : "{"name": "Heller"}"}"

1. 렉서: JSON 토큰으로 나눈다.
|{|  |"title"| |:|  |"Catch-22"| |,| ...

2. 파서: 여러 다른 의미 단위를 처리한다.
o1.setSimpleProperty("title", "Catch-22")
val o2 = o1.createObject("author")
o2.setSimpleProperty("name", "Heller")

3. 필요한 클래스의 인스턴스를 생성해 반환한다.
Book("Catch-22", Author("Heller"))
```
