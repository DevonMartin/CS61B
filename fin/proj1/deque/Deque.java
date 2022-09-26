package deque;

public interface Deque<T> {
    void addFirst(T item);
    void addLast(T item);
    /** Return true if the deque is empty, i.e. size == 0. */
    default boolean isEmpty() {
        return size() == 0;
    }
    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);
}
