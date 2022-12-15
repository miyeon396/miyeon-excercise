package effectivejava.item27;

import java.util.ArrayList;
import java.util.List;

public class UncheckedWarningExample {
    public static void main(String[] args) {
        List<Test> aList = new ArrayList();
        aList.add(new Test("test", 1));
        aList.add(new Test("test", 2));
        aList.add(new Test("test", 3));
        System.out.println("aList = " + aList);
    }

    public static class Test {
        String a;
        int b;

        public Test(String a, int b) {
            this.a = a;
            this.b = b;
        }
    }
}


