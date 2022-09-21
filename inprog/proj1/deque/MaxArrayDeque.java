package deque;

import java.util.Comparator;

class MaxArrayDeque<T> extends ArrayDeque {

    private Comparator c;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }
    public T max() {
        return (T) max(c);
    }
    public T max(Comparator<T> c) {
        System.out.println("TODO: MaxArrayDeque.max(c);");
        if (super.size() == 0) {
            return null;
        }
        int i = 0;
        T maxElement = (T) super.get(0);


        return maxElement;
    }
}
