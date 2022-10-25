package effectivejava.item11;

import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Objects;

public class HashCodeOverrideWithCacheExample {

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

        private int hashCode;

        @Override
        public int hashCode() {
            int result = hashCode;

            if (result == 0) {
                result = Short.hashCode(Short.parseShort(areaCode));
                result = 31 * result + Short.hashCode(Short.parseShort(prefix));
                result = 31 * result + Short.hashCode(Short.parseShort(lineNum));
                hashCode = result;
            }

            return result;
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
        System.out.println("s2 = " + s2); //제니


        boolean equals = jennyPhoneNum1.equals(jennyPhneNum2);
        System.out.println("equals = " + equals); //true

        int hashCode1 = jennyPhoneNum1.hashCode();
        int hashCode2 = jennyPhneNum2.hashCode();
        System.out.println("hashCode1 = " + hashCode1);
        System.out.println("hashCode2 = " + hashCode2);
        System.out.println(hashCode1==hashCode2);

        PhoneNumber johnPhoneNumber = new PhoneNumber("010", "1111", "1111");
        hashMap.put(johnPhoneNumber, "존");

        String john = hashMap.get(johnPhoneNumber);
        System.out.println("john = " + john);
        System.out.println("john hashCode = "+johnPhoneNumber.hashCode());

        System.out.println(johnPhoneNumber.equals(jennyPhoneNum1));
    }
}
