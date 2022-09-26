package deque;

import java.util.Comparator;

/**
 * An ArrayDeque that is able to calculate its maximum value
 * based on its own or another comparator.
 *
 * @param <T> is the type of data this deque works with.
 */
class MaxArrayDeque<T> extends ArrayDeque {
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
    static Comparator<Integer> getMaxIntUnderComparator(int x) {
        return new MaxIntUnderComparator(x);
    }

    private Comparator c;

    /**
     * Instantiates the deque with ArrayDeque constructor
     * plus the Comparator parameter.
     */
    MaxArrayDeque(Comparator<T> c) {
        this.c = c;
    }

    /**
     * Use the class's comparator to calculate a max value.
     * @return the max value.
     */
    T max() {
        return (T) max(c);
    }

    /**
     * Use a provided comparator to calculate a max value.
     * @param c the provided comparator.
     * @return  the max value.
     */
    T max(Comparator<T> c) {
        int size = size();
        if (size == 0) {
            return null;
        }
        T maxElement = (T) get(0);
        for (Object item: items) {
            if (!(item == null) && c.compare(maxElement, (T) item) < 0) {
                maxElement = (T) item;
            }
        }
        return maxElement;
    }
}
