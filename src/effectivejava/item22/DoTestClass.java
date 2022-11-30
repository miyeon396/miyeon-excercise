package effectivejava.item22;

public class DoTestClass {

    public static void main(String[] args) {
//        System.out.println("i want pi"+PI); //cannot resolve symbol pi
        System.out.println("i want pi"+ConstantsUsingClass.PI);
    }
}
