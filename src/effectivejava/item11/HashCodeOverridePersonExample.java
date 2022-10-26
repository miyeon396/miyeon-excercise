package effectivejava.item11;

import java.util.HashMap;

public class HashCodeOverridePersonExample {
    public static void main(String[] args) {
        Person p1 = new Person("123456-1234567", "coco" , 28);
        Person p2 = new Person("123456-1234567", "coco" , 28);

        HashMap hashMap = new HashMap();
        hashMap.put(p1, "cocoVal");

        String o = (String)hashMap.get(p1);
        System.out.println("p1을 통해 get = " + o + "hash = " + hashMap.get(p1).hashCode()); //cocoVal

        String o2 = (String)hashMap.get(p2);
        System.out.println("p2를 통해 get = " + o2 + "hash = " + hashMap.get(p2).hashCode()); //cocoVal
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

        int defaultBit = 31;
        int result = this.idNum.hashCode();

        result = defaultBit * this.name.hashCode() + result;
        result = defaultBit * Integer.hashCode(this.age) + result;

        return result;
    }
}
