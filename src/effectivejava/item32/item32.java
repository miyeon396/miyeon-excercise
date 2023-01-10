package effectivejava.item32;

public class item32 {

    public static void main(String[] args) {
//        String[] attributes = pickTwo("1번", "2번", "3번"); //실패케이스
        String[] attributes = toArray("1번", "2번", "3번"); //성공케이스
    }

    public static <T> T[] pickTwo(T a, T b, T c) {
        return toArray(a,b);
    }

    public static <T> T[] toArray(T... args) {
        return args;
    }
 }

 //pickTwo가 호출이 되게 되고 toArray의 response를 리턴하게 되는데
//이때 toArray는 무엇이냐 -> T 타입의 배열이구. T타입의 배열을 받아서 리스폰스로 리턴하게 됨 -> 이게 아마 String 배열로 리스폰스 보냄
//
//PickTwo의 입장에서는 response 할때 타입을 잘 모르게됨. 그래서 난 타입은 잘 모르겟구 Object[] args = [...]를 리턴을 할거야 하게됨
//이걸 String[]에 넣으려 하니까 에러가 나는 것.
//
//디버거 찍어보면 PickTwo가 받은 순간까지는 String[]이 나오는데 PickTwo를 나오는 순간 Object배열이 되어버림
