package deque;

import java.util.Comparator;

/**
 * An ArrayDeque that is able to calculate its maximum value
 * based on its own or another comparator.
 *
 * @param <T> is the type of data this deque works with.
 */
public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private static class MaxIntUnderComparator implements Comparator<Integer> {
        int maxInt;
        MaxIntUnderComparator(int x) {
            maxInt = x;
        }
        @Override
        public int compare(Integer a, Integer b) {
            int returnValue = Integer.compare(a, b);
            if (a > maxInt || b > maxInt) {
                return -returnValue;
            }
            return returnValue;
        }
    }
    private static Comparator<Integer> getMaxIntUnderComparator(int x) {
        return new MaxIntUnderComparator(x);
    }

    private final Comparator<T> c;

    /**
     * Instantiates the deque with ArrayDeque constructor
     * plus the Comparator parameter.
     */
    public MaxArrayDeque(Comparator<T> c) {
        this.c = c;
    }

    /**
     * Use the class's comparator to calculate a max value.
     * @return the max value.
     */
    public T max() {
        return max(c);
    }

    /**
     * Use a provided comparator to calculate a max value.
     * @param comparator the provided comparator.
     * @return           the max value.
     */
    public T max(Comparator<T> comparator) {
        int size = size();
        if (size == 0) {
            return null;
        }
        T maxElement = get(0);
        for (T item: this) {
            if (!(item == null) && comparator.compare(maxElement, item) < 0) {
                maxElement = item;
            }
        }
        return maxElement;
    }
}
