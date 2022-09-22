package deque;

import java.util.Comparator;

public class MaxIntUnder<T> implements Comparator<T> {
    int maxNumber;
    @Override
    public int compare(T o1, T o2) {
        int i1 = (int) o1;
        int i2 = (int) o2;
        int returnValue = Integer.compare(i1, i2);
        if (i1 > maxNumber || i2 > maxNumber) {
            return -returnValue;
        }
        return returnValue;
    }

    MaxIntUnder(int x) {
        maxNumber = x;
    }

    public static void main(String[] args) {
        MaxIntUnder<Integer> c1 = new MaxIntUnder<>(5);
        System.out.println(c1.compare(6000, 5000));
    }
}
