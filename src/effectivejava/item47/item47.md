# [Item47] 반환 타입으로는 스트림보다 컬렉션이 낫다

원소 시퀀스, 즉 일련의 원소를 반환하는 메서드는 수없이 많다.

이런 메서드의 반환타입으로 아래와 같은 타입을 사용했다.

- Collection, Set, List와 같은 컬렉션 인터페이스
- E[]와 같은 배열
- Iterable 인터페이스

기본은 Collection 타입이다.for-each 문에서만 쓰이거나, (contain(Object) 같은) 일부 Collection 메서드를 구현 할 수 없을 때는 Iterable 인터페이스를 사용한다.성능에 민감한 상황이면, E[] 형태의 배열을 주로 사용해 왔다.

자바 8이 스트림이라는 개념을 들고오면서 선택이 더욱 복잡해지게 되었다.

## ****Stream은 반복(loop)을 지원하지 않는다.****

Stream은 반복을 지원하지 않는다.

따라서 Stream과 반복을 알맞게 조합해야 좋은 코드가 나온다.

API를 Stream만 사용하도록 하면 for-each를 사용하고자 하는 개발자는 불편을 겪을 것이다.

(Stream은 Iterator 인터페이스가 정의한 추상메서드를 포함하고 있다. 하지만, Iterator를 확장하진 않아 for-each로 반복하지 못한다.

Stream의 for-each 반복을 위해 Stream의 iterator 메서드에 메서드 참조를 건네면 해결될 것 같다.

```java
for (ProcessHandle ph : ProcessHandle.allProcesses()::iterator) {
	// 프로세스를 처리한다.
}

//compile error
//Test.java:6: error: method reference not expected here
//for (ProcessHandle ph : ProcessHandle.allProcesses()::iterator) {
```

→ 자바 타입 추론의 한계로 컴파일되지 않는다.

이 오류를 해결하려면 메서드 참조를 매개변수화된 Iterable로 적절히 형변환해 줘야 한다.

```java
for (ProcessHandle ph : (Iterable<ProcessHandle>)
        ProcessHandle.allProcesses()::iterator) {
    // 프로세스를 처리한다.
}
```

작동은 하지만 실전에 쓰기에는 너무 난잡하고 직관성이 떨어진다.

→ 다행히 `어댑터 메서드`를 사용하면 상황이 나아진다.

자바는 이런 메서드를 제공하지 않지만 다음 코드와 같이 쉽게 만들어낼 수 있다.

이 경우에는 자바의 타입 추론이 문맥을 잘 파악하여 어댑터 메서드 안에서 따로 형변환하지 않아도 된다.

### Stream<E>를 Iterable<E>로 중개해주는 어댑터

```java
public static <E> Iterable<E> iterableOf(Stream<E> stream) {
    return stream::iterator;
}
```

어댑터를 사용하면 어떤 스트림도 for-each 문으로 반복할 수 있다.

```java
for (ProcessHandle p : iterableOf(ProcessHandle.allProcesses())) {
    // 프로세스를 처리한다. 
}
//iterableOf 메서드를 통해 명시적으로 Iterable으로 반환할 수 있다.
```

### ****API에서 Iterator만 반환하는 경우****

API에서 Iterator만 반환하는 경우에도 Stream 코드가 편한 개발자들은 불편을 겪을 수 있다.

자바는 Iterator -> Stream을 위한 어댑터를 제공하지 않지만, 손쉽게 구현이 가능하다.

```java
//Iterator<E>를 Stream<E>로 중개해주는 어댑터
public static<E> Stream<E> streamOf(Iterable<E> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false);
}
```

객체 시퀀스를 반환하는 메서드를 작성할 때, 메서드가 오직 Stream 파이프라인에서만 쓰인다면 마음놓고 `Stream을 반환`하자.

하지만 for-each를 사용하는 개발자와 Stream을 사용하는 개발자를 모두 배려하여 `Stream과 Iterable을 동시에 제공`할 수 있도록 하는 것이 좋다.

따라서 **원소 시퀀스를 반환하는 공개 API의 반환 타입에는`Collection이나 그 하위타입`을 쓰는 것이 일반적이다**

## ****컬렉션 내의 시퀀스가 크면 전용 컬렉션을 구현****

반환하는 시퀀스의 크기가 메모리에 올려도 안전할 만큼 작다면 ArrayList나 HashSet 같은 표준 컬렉션 구현체를 반환하는 게 최선일 수 있다.

하지만 단지 컬렉션을 반환한다는 이유로 `덩치 큰 시퀀스를 메모리에 올려서는 안 된다`.

### 예) ****입력 집합의 멱집합을 전용 컬렉션에 담아 반환한다.****

반환할 시퀀스가 크지만 표현을 간결하게 할 수 있다면 전용 컬렉션을 구현하는 방안을 검토.

ex) 주어진 집합의 멱집합을 반환하는 상황

<aside>
💡 멱집합이란, **한 집합의 모든 부분집합을 원소로 하는 집합**이다.

예를 들어 (a, b, c)의 멱집합은 ((), (a), (b), (c), (a, b), (a, c), (b, c), (a, b, c))이다.

→ 원소의 갯수가 n개일 때, 원소의 갯수는 2^n개가 된다.

</aside>

멱집합을 표준 컬렉션 구현체에 저장하려는 생각은 위험

하지만 `AbstractList`를 이용하면 `훌륭한 전용 컬렉션을 쉽게 구현` 가능

방법 → 멱집합을 구성하는 각 원소의 인덱스를 비트 벡터로 사용….

인덱스의 n번째 비트 값은 멱집합의 해당 원소가 원래 집합의 n번째 원소를 포함하는지 여부 알려줌

→ 0부터 2^n-1까지의 이진수와 원소 n개인 집합의 멱집합과 자연스럽게 매핑

```java
// 입력 집합의 멱집합을 전용 컬렉션에 반환
public class PowerSet {
    public static final <E> Collection<Set<E>> of(Set<E> s) {
       List<E> src = new ArrayList<>(s);
       if(src.size() > 30) {
           throw new IllegalArgumentException("집합에 원소가 너무 많습니다(최대 30개).: " + s);
       }

       return new AbstractList<Set<E>>() {
           @Override
           public int size() {
								//멱집합의 크기는 2를 원래 집합의 원소 수만큼 거듭제곱 한 것과 같음ㅁ
               return 1 << src.size();
           }

           @Override
           public boolean contains(Object o) {
               return o instanceof Set && src.containsAll((Set) o);
           }

           @Override
           public Set<E> get(int index) {
               Set<E> result = new HashSet<>();
               for (int i = 0; index != 0; i++, index >>=1) {
                   if((index & 1) == 1) {
                       result.add(src.get(i));
                   }
               }
               return result;
           }
       };
    }
}
```

- 입력 집합의 원소 수가 30을 넘으면 Power.of가 예외를 던진다.(size() 메서드의 리턴타입은 int이기 때문에 최대길이는 2^31 - 1 또는 Integer.MAX_VALUE로 제한 되기 때문)
- 이는 Stream이나, Iterable이 아닌 Collection을 쓸 때의 단점을 보여준다.(Stream이나 Iterable은 size에 대한 고민이 필요없기 때문)
- → Collection의 size 메서드가 int값을 반환하므로 PoserSet.of가 반환되는 시퀀스의 최대 길이는 Ingeger.MAX_VALUE혹은 2^31-1로 제한됨…

```java
public interface Collection<E> extends Iterable<E> {
    // Query Operations

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than {@code Integer.MAX_VALUE} elements, returns
     * {@code Integer.MAX_VALUE}.
     *
     * @return the number of elements in this collection
     */
    int size();
```

- Collection 명세에는 collection의 size가 int 범위를 넘어가는 경우 Integer.MAX_VALUE를 리턴하라고 하지만 만족스러운 해법은 아니다.

## ****Stream이 나을 때도 있다.****

위의 예제처럼 AbstractCollection을 활용해서 Collection 구현체를 리턴 할 때는 Iterator용 메서드 외에 2개만 더 구현하면 된다.바로 `contains`과 `size`이다.

하지만 반복이 시작되기 전에는 (시퀀스의 내용을 확정할 수 없는 등의 사유로) contains와 size를 구현할 수 없는 경우에는 `Collection보다는 스트림이나 Iterable을 반환하는 편이 낫다`.

원한다면 별도의 메서드를 두어 두방식 모두 제공도 가능

때로는 단순히 구현이 쉬운쪽을 선택하기도 함

### 예) ****입력 리스트의 모든 부분 리스트를 Stream으로 반환****

필요한 부분리스트를 만들어 표준 컬렉션에 담는 코드는 단 3줄

하지만 이 컬렉션은 입력 리스트의 크기의 거듭제곱만큼 메모리를 차지…

기하급수적으로 늘어나는 멱집합 보다는 낫지만 좋은 방법은 아님

```java
public class SubList {

    public static <E> Stream<List<E>> of(List<E> list) {
				//Stream.concat 반환되는 스트림에 빈 리스트 추가
        return Stream.concat(Stream.of(Collections.emptyList()), 
                             prefixes(list).flatMap(SubList::suffixes));
				//flatMap 모든 프리픽스의 서픽스로 구성된 하나의 스트림을 만든다
    }

    public static <E> Stream<List<E>> prefixes(List<E> list) {
				//rangeClosed가 반환하는 연속된 정수값들 매핑
        return IntStream.rangeClosed(1, list.size())
                        .mapToObj(end -> list.subList(0, end));
    }

    public static <E> Stream<List<E>> suffixes(List<E> list) {
				//range가 반환하는 연속된 정수값들 매핑
        return IntStream.range(0, list.size())
                        .mapToObj(start -> list.subList(start, list.size()));
    }
}
```

- (a, b, c)의 prefixes는 (a), (a, b), (a, b, c) 이다
- (a, b, c)의 suffixes는 (c), (b, c), (a, b, c) 이다
- 어떤 리스트의 부분리스트는 단순히 그 리스트의 프리픽스의 서픽스에 빈 리스트 하나만 추가하면 된다.
- Stream.concat 메서드는 반환되는 스트림에 빈 리스트를 추가하며, flatMap 메서드(45item)는 모든 프리픽스의 모든 서픽스로 구성된 하나의 스트림을 만듦
- 프리픽스와 서픽스들의 스트림은 IntStream.range와 IntStream.rangeClosed가 반환하는 연속된 정수값들을 매핑해서 만듦
- 정수 인덱스를 사용한 표준 for문의 스트림 버전이라고 볼 수 있음…

****위의 내용과 같은 로직 - for loop를 이용한 코드****

prefixes, suffixes 메서드는 아래 for 반복문을 중첩해 만든 것과 취지가 비슷

```java
for (int start = 0; start < src.size(); start++) {
    for (int end = start + 1; end <= src.size(); end++) {
        System.out.println(src.subList(start, end));
    }
}
```

위****의 로직과 같은 로직 - Stream 중첩으로 변경****

```java
public static <E> Stream<List<E>> of(List<E> list) {
    return IntStream.range(0, list.size())
        .mapToObj(start -> 
                  IntStream.rangeClosed(start + 1, list.size())
                           .mapToObj(end -> list.subList(start, end)))
        .flatMap(x -> x);
}
```

## 요약

- Stream이나 Iterable을 리턴하는 API에는 Stream -> Iterable, Iterable -> Stream으로 변환하기 위한 `어댑터 메서드`가 필요하다.
- 어댑터는 클라이언트 코드를 어수선하게 만들고 더 느리다 (책에서는 2.3배정도 느리다함)
- 원소 시퀀스를 반환하는 메서드를 작성할 때는 Stream, Iterator를 모두 지원할 수 있게 작성하자(되도록 Collection으로 하는게 좋다.)
- 원소의 갯수가 많다면, 멱집합의 예처럼 `전용 컬렉션을 리턴하는 방법도 고민`하자
- 만약 나중에 Stream 인터페이스가 Iterable을 지원하도록 수정된다면, 그때는 안심하고 Stream을 반환하면 된다.