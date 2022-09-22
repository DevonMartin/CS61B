package deque;

import java.util.Comparator;

/**
 * An ArrayDeque that is able to calculate its maximum value
 * based on its own or another comparator.
 *
 * @param <T> is the type of data this deque works with.
 */
class MaxArrayDeque<T> extends ArrayDeque {

    private Comparator c;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }

    /**
     * Use the class's comparator to calculate a max value.
     * @return the max value.
     */
    public T max() {
        return (T) max(c);
    }

    /**
     * Use a provided comparator to calculate a max value.
     * @param c the provided comparator.
     * @return  the max value.
     */
    public T max(Comparator<T> c) {
        int size = size();
        if (size == 0) {
            return null;
        }
        T maxElement = (T) get(0);
        for (int i = 1; i < size; i++) {
            T item = (T) get(i);
            if (c.compare(maxElement, item) < 0) {
                maxElement = item;
            }
        }
        return maxElement;
    }
}
