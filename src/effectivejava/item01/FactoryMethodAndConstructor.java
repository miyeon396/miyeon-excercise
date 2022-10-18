package effectivejava.item01;

public class FactoryMethodAndConstructor {

    public static void main(String[] args) {
//        Coco myCoco = new Coco();
//        System.out.println("myCoco = " + myCoco.getAge()+"="+myCoco.getColor());
//
//        Coco myCoco2 = new Coco(1, "white");
//        System.out.println("myCoco2 = " + myCoco2.getAge()+"="+myCoco2.getColor());

        Coco myCoco3 = Coco.valueOf(2, "black");
        System.out.println("myCoco3 = " + myCoco3.getAge()+"="+myCoco3.getColor());

        //객체 생성자 private로 막아놓고 static factory method로만 객체가 생성이 가능하게.
        //정적 팩토리 메서드를 추가한다고 해서 생성자를 꼭 private로 무조건 선언할 필요는 없으나, 굳이 정적 팩토리로 제한을 하는만큼 private로 두는거지 싶다.
    }

}

class Coco {
    int age;
    String color;

    public int getAge() {
        return age;
    }

    public String getColor() {
        return color;
    }

    private Coco() { //얘는 따로 선언 안해도됨 그치만 static 추가한 후 기본생성자 주석 거니까 위에 myCoco도 컴파일 에러 뱉음 -> 자동으로 private로 인식이 되는거 같다
    }

    private Coco(int age, String color) {
        this.age = age;
        this.color = color;
    }

    public static Coco valueOf(int age, String color) {
        return new Coco(age, color);
    }
}
