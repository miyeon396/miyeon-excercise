# [Item37] Ordinal 인덱싱 대신 EnumMap을 사용하라

---

## Ordinal 기반 인덱싱

배열이나 리스트에서 원소를 꺼낼 때 ordinal 메서드로 인덱스를 얻는 코드

식물의 생애주기를 열거 타입으로 표현한 LifeCycle 열거 타입 예시

```java
public class Plant {

    enum LifeCycle { ANNUAL, PERNNIAL, BIENNIAL}

    final String name;
    final LifeCycle lifeCycle;

    public Plant(String name, LifeCycle lifeCycle) {
        this.name = name;
        this.lifeCycle = lifeCycle;
    }

    @Override
    public String toString() {
        return name;
    }
}
```

정원에 심은 식물들을 배열 하나로 관리하고, 생애 주기별로 묶음

```java
public static void usingOrdinalArray(List<Plant> garden) {
    Set<Plant>[] plantsByLifeCycle = 
				(Set<Plant>[]) new Set[LifeCycle.values().length];

    for (int i = 0 ; i < plantsByLifeCycle.length ; i++) {
        plantsByLifeCycle[i] = new HashSet<>();
    }

    for (Plant plant : garden) {
        plantsByLifeCycle[plant.lifeCycle.ordinal()].add(plant);
    }

    for (int i = 0 ; i < plantsByLifeCycle.length ; i++) {
        System.out.printf("%s : %s%n", LifeCycle.values()[i], plantsByLifeCycle[i]);
    }
}
```

위의 코드는 열거타입의 Ordinal을 배열의 인덱스로 사용하는 코드.

1. Set 배열을 생성해 생애 주기별로 관리.
    - 총 3개의 배열이 만들어 짐
    - 각 배열을 순회하여 빈 HashSet으로 초기화
2. Plant들을 배열의 Set에 추가.
    - 이 때 Plant가 갖고 있는 Lifecycle 열거 타입의 Ordinal값으로 배열의 인덱스를 결정.
    - 그 결과로 식물의 생애주기 별로 Set에 추가가 됨.
3. 결과 출력
    - 열거타입의 values로 반환되는 열거 타입 상수 배열의 순서는 Ordinal 값으로 결정되기 때문에
    - Set 배열의 각 Set이 의미하는 생애 주기는 values의 순서와 같을 것

### 해당 코드의 문제

1. 배열은 제네릭과 호환되지 않는다. 따라서 비검사 형변환을 수행해야한다.
2. 사실상 배열은 각 인덱스가 의미하는 바를 알지 못하기 때문에 출력 결과에 직접 레이블을 달아야한다.
3. 정수는 열거 타입과 달리 타입 안전하지 않기 떄문에 정확한 정숫값을 사용한다는 것을 직접 보증해야한다.

## EnumMap

이러한 단점들을 java.util 패키지의 EnumMap을 사용하여 해결.

EnumMap은 열거 타입을 키로 사용하는 Map의 구현체

```java
public static void usingEnumMap(List<Plant> garden) {
    Map<LifeCycle, Set<Plant>> plantsByLifeCycle = 
				new EnumMap<>(LifeCycle.class);

    for (LifeCycle lifeCycle : LifeCycle.values()) {
        plantsByLifeCycle.put(lifeCycle,new HashSet<>());
    }

    for (Plant plant : garden) {
        plantsByLifeCycle.get(plant.lifeCycle).add(plant);
    }

    //EnumMap은 toString을 재정의하였다.
    System.out.println(plantsByLifeCycle);
}
```

1. 이전 Ordinal을 사용한 코드와 다르게 안전하지 않은 형변환을 사용하지 않음
2. 결과를 출력하기 위해 번거롭던 과정도 EnumMap 자체가 toString을 제공하기 때문에 번거롭지 않게 됨
3. Ordinal을 이용한 배열 인덱스를 사용하지 않으니 인덱스를 계산하는 과정에서 오류가 날 가능성이 존재하지 않음
4. EnumMap은 그 내부에서 배열을 사용하기 때문에 내부 구현 방식을 안으로 숨겨 Map의 타입 안정성과 배열의 성능을 모두 얻어냄

→ Stream을 사용하면 코드를 더 줄일 수 있다.

```java
public static void streamV1(List<Plant> garden) {
    Map plantsByLifeCycle = garden.stream().collect(Collectors.groupingBy(plant -> plant.lifeCycle));
    System.out.println(plantsByLifeCycle);
}

public static void streamV2(List<Plant> garden) {
    Map plantsByLifeCycle = garden.stream().collect(Collectors.groupingBy(plant -> plant.lifeCycle,
                    () -> new EnumMap<>(LifeCycle.class),Collectors.toSet()));
    System.out.println(plantsByLifeCycle);
}
```

Collectors의 groupingBy 메서드를 이용해 맵 구성.

StreamV1 / StreamV2 메서드 차이는 groupingBy 메서드에 원하는 맵 구현체를 명시 하였는가의 차이

### StreamV1

- EnumMap이 아닌 고유한 맵 구현체를사용했기 때문에 EnumMap을 써서 얻는 공간과 성능 이점이 사라진다.
- 존재하는 열거 타입 상수만 Key를 만든다.

### StreamV2

- EnumMap 버전은 열거 타입 상수 별로 하나씩 Key를 전부 다 만듦.

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/0e2b727b-732f-4777-8878-9a797a8d1b71/Untitled.png)

## 좀 더 복잡한 EnumMap 사용

두가지 상태(Phase)를 전이(Transition)와 매핑하는 예제.

LIQUID → SOLID 전이는 FREEZE

LIQUID → GAS 전이 BOIL

해당 예제 역시 Phase나 Transition의 상수의 선언 순서를 변경하거나 새로운 Phase 상수를 추가하는 경우 문제가 발생할 수 있음.

```java
public enum Phase {
    SOLID, LIQUID, GAS;

    public enum Transition {
        MELT, FREEZE, BOIL, CONDENSE, SUBLIME, DEPOSIT;

        private static final Transition[][] TRANSITIONS = {
                {null, MELT, SUBLIME},
                {FREEZE, null, BOIL},
                {DEPOSIT, CONDENSE, null}
        };

        public static Transition from(Phase from, Phase to) {
            return TRANSITIONS[from.ordinal()][to.ordinal()];
        }
    }
}
```

EnumMap을 사용

```java
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum Phase {

    SOLID, LIQUID, GAS;

    public enum Transition {
        
        MELT(SOLID, LIQUID),
        FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS),
        CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS),
        DEPOSIT(GAS, SOLID);

        private final Phase from;
        private final Phase to;

        Transition(Phase from, Phase to) {
            this.from = from;
            this.to = to;
        }

        private static final Map<Phase, Map<Phase, Transition>> transitionMap = Stream.of(values())
                .collect(Collectors.groupingBy(t -> t.from, // 바깥 Map의 Key
                        () -> new EnumMap<>(Phase.class), // 바깥 Map의 구현체
                        Collectors.toMap(t -> t.to, // 바깥 Map의 Value(Map으로), 안쪽 Map의 Key
                                t -> t, // 안쪽 Map의 Value
                                (x,y) -> y, // 만약 Key값이 같은게 있으면 기존것을 사용할지 새로운 것을 사용할지
                                () -> new EnumMap<>(Phase.class)))); // 안쪽 Map의 구현체;

        public static Transition from(Phase from, Phase to) {
            return transitionMap.get(from).get(to);
        }
    }

}
```

전이 부분 복잡…

Map<Phase, Map<Phase, Transition>>은 “이전 상태에서 ‘이후상태에서 전이로의 맵’에 대응시키는 맵”

이러한 맵의 맵을 초기화하기 위해 수집기 2개(stream.Collector)를 차례로 사용

1. 첫번째 수집기 groupingBy
    - 전이를 이전상태를 기준으로 묶음
2. 두번째 수집기 toMap
    - 이후 상태를 전이에 대응 시키는 EnumMap을 생성

## 해당 코드에 PLASMA를 추가

### 배열

배열로 만든 코드를 수정하려면 새로운 상수를 Phase에 1개, Phase.Transition에 2개 추가하고

원소 9개짜리인 배열들의 배열을 원소 16개짜리로 교체.. → 적거나 많은 원소 기입, 순서 이슈 등 런타임 이슈 증가..

### EnumMap

전이 목록에 IONIZE(GAS, PLASMA)와 DEIONIZE(PLASMA, GAS)만 추가하면 끝

```java
public enum Phase {
    SOLID, LIQUID, GAS, PLASMA;

    public enum Transition {
        MELT(SOLID, LIQUID),
        FREEZE(LIQUID, SOLID),
        BOIL(LIQUID, GAS),
        CONDENSE(GAS, LIQUID),
        SUBLIME(SOLID, GAS),
        DEPOSIT(GAS, SOLID),
        IONIZE(GAS, PLASMA),
        DEIONIZE(PLASMA, GAS);
    }
    
    //나머지 코드는 그대로
    
}
```

나머지 부분은 기존 로직에서 잘 처리해주어 잘못 수정할 가능성이 극히 적음.

실제 내부에는 맵들의 맵이 배열들의 배열로 구현되니 낭비되는 공간과 시간도 거의 없이 명확하고 안전하고 유지보수 하기 좋다.

## 정리

배열의 인덱스를 얻기 위해 ordinal을 쓰는 것은 일반적으로 좋지 않으니, 대신 `EnumMap을 사용하라`