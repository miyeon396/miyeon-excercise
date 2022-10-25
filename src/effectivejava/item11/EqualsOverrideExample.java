package effectivejava.item11;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Objects;

public class EqualsOverrideExample {

    static class PhoneNumber {
        public final String areaCode;
        public final String prefix;
        public final String lineNum;

        public PhoneNumber(String areaCode, String prefix, String lineNum) {
            this.areaCode = areaCode;
            this.prefix = prefix;
            this.lineNum = lineNum;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            PhoneNumber that = (PhoneNumber) obj;
            return Objects.equals(areaCode, that.areaCode) && Objects.equals(prefix, that.prefix) && Objects.equals(lineNum, that.lineNum);
        }
    }

    @Test
    public void hashMapTest1() {
        HashMap<PhoneNumber, String> hashMap = new HashMap<>();

        PhoneNumber jennyPhoneNum1 = new PhoneNumber("010", "1111", "1111");
        hashMap.put(jennyPhoneNum1, "제니");
        String s1 = hashMap.get(jennyPhoneNum1);
        System.out.println("s1 = " + s1); //제니

        PhoneNumber jennyPhneNum2 = new PhoneNumber("010", "1111", "1111");
        String s2 = hashMap.get(jennyPhneNum2);
        System.out.println("s2 = " + s2); //null


        boolean equals = jennyPhoneNum1.equals(jennyPhneNum2);
        System.out.println("equals = " + equals); //true

        int hashCode1 = jennyPhoneNum1.hashCode();
        int hashCode2 = jennyPhneNum2.hashCode();
        System.out.println("hashCode1 = " + hashCode1);
        System.out.println("hashCode2 = " + hashCode2);
        System.out.println(hashCode1==hashCode2);
    }
}
