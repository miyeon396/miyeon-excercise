package effectivejava.item07;

import java.util.Arrays;
import java.util.EmptyStackException;

public class Stack {
    private Object[] elements; // -> 스택이 커졌다가 줄어들었을 때 스택에서 꺼내진 객체들을 가비지 컬렉터가 회수하지 않는다. 프로그램에서 그 객체들을 더이상 사용하지 않더라도
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size==0) {
            throw new EmptyStackException();
        }
        Object result = elements[--size];
        elements[size] = null; // 다쓴 참조 해제
        return result;
    }

    /**
     * 원소를 위한 공간을 적어도 하나 이상 확보한다.
     * 배열의 크기를 늘려야할 때마다 대략 두배씩 늘린다.
     */
    private void ensureCapacity() {
        if (elements.length==size) {
            elements = Arrays.copyOf(elements, 2 * size + 1);
        }
    }
}
