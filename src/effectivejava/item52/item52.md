# [Item52] 다중정의는 신중히 사용하라

---

## 다중 정의(Overloading)

이름이 같은 메서드가 매개변수의 타입이나 개수만 다르게 갖는 형태

이러한 다중 정의를 사용할 때는 신중해야 한다.

컬렉션을 구분하기 위한 프로그램

```java
public class CollectionClassifier {
    public static String classify(Set<?> s) {
        return "집합";
    }

    public static String classify(List<?> lst) {
        return "리스트";
    }

    public static String classify(Collection<?> c) {
        return "그 외";
    }

    public static void main(String[] args) {
        Collection<?>[] collections = {
                new HashSet<String>(),
                new ArrayList<BigInteger>(),
                new HashMap<String, String>().values()
        };

        for (Collection<?> c : collections)
            System.out.println(classify(c));
    }
}
```

- 코드만 봤을 때는 의도대로 각 타입별로 구분되어 출력이 될 것 만 같은 기분
- 하지만 “그 외”만 3번 출력
- `다중정의`된 classify() 메서드는 `컴파일 타임`에 어떤 메서드가 호출될 것인지 정해지기 때문
- for 문 안의 Collection<?> c는 `런타임에 타입이 결정`되고 달라짐. 
→ `컴파일 타임에는 항상 Collection<?> 타입임`
- 이처럼 직관과 어긋나게 동작하는 이유
    - `재정의(override)한 메서드는 동적`으로 선택되고, `다중정의(overload)한 메서드는 정적`으로 선택되기 때문
- 해당 예제의 의도는 매개변수의 런타임 타입에 기초해 적절한 다중정의 메서드로 자동 분배 하는 것. 하지만 의도대로 동작하지 않았음. 문제 해결을 위해 메서드 합친 후 instanceof로 명시적 검사 수행하면 해결

    ```java
    public static String classify(Collection<?> c) {
        return c instanceof Set  ? "집합" :
                c instanceof List ? "리스트" : "그 외";
    }
    ```


## 재정의(Overriding)

재정의란 상위 클래스의 메서드를 하위 클래스에서 재정의 하는 것을 뜻한다.

메서드를 `재정의` 했다면 해당 `객체의 런타임 타입이 어떤 메서드를 호출할 지의 기준`이 된다.

재정의한 메서드는 런타임에 어떤 메서드를 호출할지 정해진다.

```java
class Wine {
    String name() { return "포도주"; }
}

class SparklingWine extends Wine {
    @Override String name() { return "발포성 포도주"; }
}

class Champagne extends SparklingWine {
    @Override String name() { return "샴페인"; }
}

public class Overriding {
    public static void main(String[] args) {
        List<Wine> wineList = List.of(
                new Wine(), new SparklingWine(), new Champagne());

        for (Wine wine : wineList)
            System.out.println(wine.name());
    }
}
```

- 포도주 - 발포성 포도주 - 샴페인 차례대로 출력
- for 문에서의 컴파일 타임 타입이 모두 Wine인 것에 무관하게 항상 `가장 하위에서 정의한 재정의 메서드`가 실행

## 다중정의가 혼동을 일으키는 상황을 피하자

- API 사용자가 매개변수를 넘길 때, 어떤 다중정의 메서드가 호출될지 모른다면 프로그램은 오작동하기 쉽다.
- 헷갈릴 수 있는 코드는 작성하지 말자(ex-다중정의 예시)
- 안전하고 보수적으로 가려면 매개변수 수가 같은 다중정의는 만들지 말자
- 가변인수를 매개변수로 사용한다면 다중정의는 사용하면 안된다.

이 규칙들만 잘 따르면 다중정의가 혼동을 일으키는 일은 피할 수 있다. 이 외에 다중정의하는 대신 메서드 이름을 다르게 지어주는 방법도 존재한다.

### 예제

ObjectOuputStream 클래스.

이 클래스의 write 메서드는 모든 기본 타입과 일부 참조 타입용 변형을 가지고 있다.

다중 정의가 아닌, `모든 메서드에 다른 이름을 지어`주는 길을 택했다.

```java
....
    public void writeBoolean(boolean val) throws IOException {
        bout.writeBoolean(val);
    }

    /**
     * Writes an 8 bit byte.
     *
     * @param   val the byte value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    public void writeByte(int val) throws IOException  {
        bout.writeByte(val);
    }

    /**
     * Writes a 16 bit short.
     *
     * @param   val the short value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    public void writeShort(int val)  throws IOException {
        bout.writeShort(val);
    }

    /**
     * Writes a 16 bit char.
     *
     * @param   val the char value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    public void writeChar(int val)  throws IOException {
        bout.writeChar(val);
    }

    /**
     * Writes a 32 bit int.
     *
     * @param   val the integer value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    public void writeInt(int val)  throws IOException {
        bout.writeInt(val);
    }

    /**
     * Writes a 64 bit long.
     *
     * @param   val the long value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    public void writeLong(long val)  throws IOException {
        bout.writeLong(val);
    }

    /**
     * Writes a 32 bit float.
     *
     * @param   val the float value to be written
     * @throws  IOException if I/O errors occur while writing to the underlying
     *          stream
     */
    public void writeFloat(float val) throws IOException {
        bout.writeFloat(val);
    }
....
```

→ 굳이 다중정의를 사용하지 않아도, 이렇게 메서드 이름을 다르게 가는 것으로 좀 더 나은 코드가 될 수 있다.

## 생성자 다중정의

- 생성자는 이름을 다르게 지을 수 없으니 `두번째 생성자`부터는 `무조건 다중정의`가 된다.
- 이러한 상황에 `정적 팩터리 메서드`가 적절한 대안이 될 수 있다.
- 또한 생성자는 재정의할 수 없으니 다중정의와 재정의가 혼용될 걱정도 없다.
- 그래도 여러 생성자가 같은 수의 매개변수를 받아야 하는 경우는 피할 수 없으니 그에 따른 `안전대책`을 배워야 한다.

## 안전 대책

- 매개변수 수가 같은 다중정의 메서드가 많더라도 근본적으로 다르면 헷갈릴 일이 없다.
- 근본적으로 다르다는 것은 `두 타입의 값을 어느쪽으로든 형변환 할 수 없다`는 뜻

이 조건을 충족하면 어느 다중정의 메서드를 호출할지가 매개변수들의 런타임 타입만으로 결정된다.

따라서 컴파일 타임 타입에는 영향을 받지 않게 되고, 혼란을 주는 주된 원인이 사라진다.

## 다중 정의 시 주의를 기울여야 하는 이유 1 (오토박싱)

### Set 예시

```java
public class SetList {
    public static void main(String[] args) {
        Set<Integer> set = new TreeSet<>();

        for (int i = -3; i < 3; i++) {
            set.add(i);
        }

        for (int i = 0; i < 3; i++) {
            set.remove(i);
        }
        System.out.println(set);
    }

}
```

- [-3, -2, -1, 0, 1, 2]의 값에서 [0,1,2]를 지우니 [-3,-2,-1]이 출력되어야 할 것 같다.
- 실제로 [-3,-2,-1]이 출력되면서 테스트가 통과한다. Set의 remove() 메서드의 시그니처는 remove(Object)기 때문에 정상적으로 0이상의 값을 지운다.

  ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/fe6af222-27f6-4d3d-a032-9ca9e7a33656/Untitled.png)


### List 예시

```java
public class SetList {
    public static void main(String[] args) {
       List<Integer> list = new ArrayList<>();

        for (int i = -3; i < 3; i++) {
            list.add(i);
        }
        for (int i = 0; i < 3; i++) {
            list.remove(i);
        }
        System.out.println(list);
    }

}
```

- 해당 코드도 마찬가지로 [-3, -2, -1]이 출력되어야 할 것 같다.
- 하지만 전혀 다른 결과가 출력됨 [-2,0,2]

  ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b40c22c8-b0e9-406b-8ef9-8f5eeebbdb89/Untitled.png)

    - List의 remove가 다중정의(overloading) 되어있기 때문에 [-2,0,2] 출력

      ![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/7d009e7b-3d9a-47c9-9671-54029bfa95a8/Untitled.png)

    - 위의 코드에서는 remove(Obejct)가 아닌 `remove(int index) 메서드`가 선택된다. 따라서 값이 아닌 index의 원소를 제거하기 때문에 [-2,0,2]가 출력됨
- Java4까지는 Object와 int가 근본적으로 달라 문제가 없었지만, Java5부터 오토 박싱이 도입되면서 이 개념이 흐트러 짐
- = 이제는 int와 Integer가 근본적으로 다르지 않다는 것.
- 해당 이슈는 remove를 호출 할 때 `매개변수를 Integer로 형 변환`해주면 해결됨
- Set,List 예시에서 중요한 점 제네릭과 오토박싱이 추가 되면서 기존 List인터페이스가 취약해졌다. (다중 정의에 의해)
- → 해당 예시만으로도 다중정의를 왜 신중하게 사용해야 하는지에 대한 충분한 근거가 됨

## 람다와 메서드 참조의 혼란 예제

```java
// 1번. Thread의 생성자 호출
new Thread(System.out::println).start();

// 2번. ExecutorService의 submit 메서드 호출
ExecutorService exec = Executors.newCachedThreadPool();
exec.submit(System.out::println);
```

- 1,2번의 모습은 비슷하지만 2번은 컴파일 오류가 발생한다.
- 넘겨진 인수는 ㅁ두 System.out::println으로 똑같고 양쪽 모두 Runnable을 받는 형제 메서드를 다중 정의하고 있음
- 왜 한쪽만 실패?
    - submit 다중 정의 메서드 중에는 Callable<T>를 받는 메서드도 있고,
    - 모든 println이 void를 반환하니, void를 반환하는 println의 경우 정상적으로 작동할거라고 예상했겠지만, println도 다중정의 되어있는 메서드기 떄문에 예상과 다르게 동작된다.

```java
...
// println의 다중정의 PrintStream.java
    public void println() {
        newLine();
    }

    /**
     * Prints a boolean and then terminate the line.  This method behaves as
     * though it invokes {@link #print(boolean)} and then
     * {@link #println()}.
     *
     * @param x  The {@code boolean} to be printed
     */
    public void println(boolean x) {
        synchronized (this) {
            print(x);
            newLine();
        }
    }

    /**
     * Prints a character and then terminate the line.  This method behaves as
     * though it invokes {@link #print(char)} and then
     * {@link #println()}.
     *
     * @param x  The {@code char} to be printed.
     */
    public void println(char x) {
        synchronized (this) {
            print(x);
            newLine();
        }
    }
...
```

- System.out::println은 부정확한 메서드 참조다.
- 암시적 타입 람다식이나 부정확한 메서드 참조 같은 인수 표현식은 목표 타입이 선택되기 전에는 그 의미가 정해지지 않기 때문에 적용성 테스트 때 무시된다.
- → 문제의 원인
- 핵심
    - 서로 다른 함수형 인터페이스라도 인수 위치가 같으면 혼란이 생긴다.
    - 메서드를 `다중정의`할 떄 서로 다른 함수형 인터페이스라도 같은 위치의 인수로 받아서는 안된다.
    - 서로 다른 함수형 인터페이스라도 서로 근본적으로 다르지 않아 컴파일러의 경고가 발생할 것

## Forward 방법

상대적으로 더 특수한 다중정의 메서드에서 덜 특수한(더 일반적인) 다중정의 메서드로 일을 넘겨 버리는 것

- 다중정의 된 메서드 중 하나를 선택하는 규칙은 매우 복잡하며, 근본적으로 다르다 라는 두 클래스의 경우도 위 println 처럼 예외가 있을 수 있으니 구분하기가 매우 어렵다.
- 다중정의 메서드의 기능이 동일하다면 신경 쓸게 없다.
- 인수를 포워드하여 두 메서드가 동일한 일을 하도록 보장한다.

```java
public boolean contentEquals(StringBuffer sb) {
    return contentEquals((CharSequence) sb);
}
// 인수를 포워드 하여 기존의 메서드 contentEqauls를 호출하면 된다.
```

## 핵심 정리

- 일반적으로 매개변수 수가 같을 때는 다중정의를 피하는게 좋다.
- 만약 다중정의를 피할 수 없는 상황이라면 헷갈릴만한 매개변수는 `형변환`하여 `정확한 다중정의 메서드가 선택`되도록 하자