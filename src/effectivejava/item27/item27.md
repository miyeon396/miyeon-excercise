# Item27. 비검사 경고를 제거하라

---

## 에러 vs 경고

컴파일러가 컴파일 할 때 **에러**와 **경고**두가지로 나누어진다.

- **에러**는 컴파일이 실패하는 것이고,**경고**는 컴파일에 실패하지는 않지만 권장하지 않고, 위험성이 있을 때 나타난다.
- **경고**는 컴파일러시 **경고 메세지**를 출력할 수 있다.
- 이 경고 중에서도 **비검사 (unchecked)**란 **컴파일러가 타입 안정성을 확인하는데 필요한 정보가 충분치 않을 때** 발생하는 경고이다.

```java
public class SetExample {

    public static void main(String[] args) {
        Set names = new HashSet();

        Set<String> strings = new HashSet();
    }
}
```

**로 타입**을 사용한다면 **비검사 경고**가 발생한다.

**비검사 경고**가 발생했을 때는 다음 두가지 규칙을 따라야한다.

- 비검사 경고를 제거할 수 있으면 가능한한 모두 제거해야한다.
- 제거할 수 없는 경우`@SuppressWarnings`을 붙여주어야 한다.

→ **다이아몬드 연산자 (<>)**를 사용해 수정해준다.

→ Set<String> strings = new HashSet<>();

→ 비검사 경고 해결 시 타입 안정성이 보장

## **@SuppressWarnings**

만약, 경고를 제거할 수는 없지만 **타입 안전하다고 확신**할 수 있다면`@SuppressWarnings("unchecked")`를 이용해 비검사 경고를 숨기자.

타입 안전하다고 검증된 코드의 검사를 그대로 두면 진짜 문제를 알리는 경고 코드를 구분하기 쉽지 않다.

또한 타입 안전함을 검증하지 않은 채 경고를 숨기면 잘못된 보안인식을 심어주는 꼴이된다.

### @SuppressWarnings("uncheck") 사용범위

`@SuppressWarnings("unchecked")`는 가능한 좁은 범위에 적용

`@SuppressWarnings`애너테이션은 개별 지역변수 선언부터 클래스 전체까지 어떤 선언에도 달 수 있다.

한줄이 넘는 메서드나 생성자에`@SuppressWarnings`가 달려있다면 지역변수나 아주 짧은 메서드 혹은 생성자로 옮기자.

절대로 클래스 전체에 적용해서는 안된다.

**as-is**

```java
public <T> T[] toArray(T[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
}
```

위 코드를 컴파일하면`@copyOf()` 부분에서 경고가 발생한다. 이 경고를 제거하려면 지역변수를 추가해야 한다.

`return`문에는`@SuppressWarnings("unchecked")`를 다는게 불가능하기 때문이다.

**to-be**

```java
public <T> T[] toArray(T[] a) {
    if (a.length < size)
        // 생성한 배열과 매개변수로 받은 배열이 모두 T[]로 같으므로
        // 올바른 형변환이다.
        @SuppressWarnings("unchecked")
        T[] result = (T[]) Arrays.copyOf(elementData, size, a.getClass());
        return result
    System.arraycopy(elementData, 0, a, 0, size);
    if (a.length > size)
        a[size] = null;
    return a;
}
```

이 코드는 깔끔하게 컴파일되고 비검사 경고를 숨기는 범위도 최소로 좁혔다.

**`@SuppressWarnings("unchecked")`에너테이션을 사용할 때면 그 경고를 무시해도 안전한 이유를 항상 주석으로 남겨야한다.**

다른 사람이 해당 코드를 이해하는데 도움이되며, 그 코드를 잘못 수정해 타입 안정성을 잃는 상황을 줄여주기 때문이다.

## 정리

비검사 경고는 런타임에`@ClassCastException`을 일으킬 수 있는 잠재적 가능성을 의미하니 가능한한 제거하자.

만약, 경고를 제거하기 힘들다면 타입 안정성을 증명하고`@SuppressWarnings("unchecked")`애너테이션으로 경고를 숨기고 그 근거를 주석으로 남기자.