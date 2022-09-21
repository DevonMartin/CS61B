package deque;

import java.util.Comparator;

public class Comparator1<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        return Integer.compare(1, 2);
    }

    Comparator1(T t) {

    }

    public static void main(String[] args) {
        Comparator1<Integer> c1 = new Comparator1<>(5);
        System.out.println(c1.compare(5, 5));
    }
}
