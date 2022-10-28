package effectivejava.item11;

import java.util.HashMap;
import java.util.Objects;

public class HashCodeOverridePersonExample {
    public static void main(String[] args) {
        Person p1 = new Person("123456-1234567", "coco" , 28);
        Person p2 = new Person("123456-1234567", "coco" , 28);
        Person p3 = new Person("111111-1111111", "popo" , 30);

        HashMap hashMap = new HashMap();
        hashMap.put(p1, "cocoVal");
        hashMap.put(p3, "popoVal");

        String o = (String)hashMap.get(p1);
        System.out.println("p1을 통해 get = " + o + " hash = " + p1.hashCode()); //cocoVal

        String o2 = (String)hashMap.get(p2);
        System.out.println("p2를 통해 get = " + o2 + " hash = " + p2.hashCode()); //cocoVal

        String o3 = (String)hashMap.get(p3);
        System.out.println("p3를 통해 get = " + o3 + " hash = " + p3.hashCode()); //popoVal
    }
}


class Person {
    private String idNum;
    private String name;
    private int age;

    public Person(String idNum, String name, int age) {
        this.idNum = idNum;
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Person)) {
            return false;
        }

        Person person = (Person) o;

        return this.idNum.equals(person.idNum) && this.name.equals(person.name) && this.age == person.age;
    }


    @Override
    public int hashCode() {
        //1. int 변수 result를 선언한 후 값c로 초기화한다.
        //    - 이때 c는 해당 객체의 첫번째 핵심 필드를 계산한 것 (핵심필드 : equals 비교에 사용되는 필드)
        //2. 해당 필드의 해시코드 c를 계산
        //    1. 기본 타입 필드,
        //       Type.hashCode(f)를 수행. (Type은 해당 기본 타입의 박싱 클래스)
        //    2. 참조 타입,
        //       참조 타입의 hashCode 호출. 필드의 값이 null이면 0 사용
        //       필드면서 이 클래스의 equals 메서드가 이 필드의 equals를 재귀적으로 호출해 비교한다면, 이 필드의 hashCode를 재귀적으로 호출한다. 계산이 복잡해질 것 같으면 이 필드의 표준형을 만들어 그 표준형의 hashCode를 호출한다. 필드의 값이 null이면 0을 사용한다.
        //    3. 필드가 배열이라면,
        //       핵심 원소 각각을 별도 필드처럼 다룬다. 배열에 핵심 원소가 하나도 없다면 단순히 상수 0을 사용한다. 모든 원소가 핵심 원소라면 Arrays.hashCode를 사용한다.
        //    4. 위에서 계산한 해시코드 c로 result를 갱신한다.
        //       result = 31 * result + C;
        //3. result를 반환한다.

        int defaultBit = 31;
        int result = this.idNum.hashCode();

        result = defaultBit * this.name.hashCode() + result;
        result = defaultBit * Integer.hashCode(this.age) + result;

        return result;
//        return Objects.hash(idNum, name, age);
    }
}
