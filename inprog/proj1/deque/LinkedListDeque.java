package deque;

public class LinkedListDeque<T> {
    /** Subclass utilized by LinkedListDeque
     *
     * @param <T> the item stored at the node.
     */
    private class LinkedList<T> {
        T t;
        LinkedList next;
        LinkedList last;
        LinkedList(T t) {
            this.t = t;
        }
    }
    private int size;
    private LinkedList sentinel;

    public LinkedListDeque() {
        sentinel = new LinkedList(0);
        sentinel.next = sentinel;
        sentinel.last = sentinel;
        size = 0;
    }
    public void addFirst(T t) {
        LinkedList tmp = new LinkedList(t);
        tmp.next = sentinel.next;
        tmp.next.last = tmp;
        tmp.last = sentinel;
        sentinel.next = tmp;
        size++;
    }
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T returnT = (T) sentinel.next.t;
        sentinel.next = sentinel.next.next;
        sentinel.next.last = sentinel;
        size--;
        return returnT;
    }
    public void addLast(T t) {
        LinkedList tmp = new LinkedList(t);
        tmp.last = sentinel.last;
        tmp.last.next = tmp;
        tmp.next = sentinel;
        sentinel.last = tmp;
        size++;
    }
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returnT = (T) sentinel.last.t;
        sentinel.last = sentinel.last.last;
        sentinel.last.next = sentinel;
        size--;
        return returnT;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public T get(int index) {
        if (index < size / 2) {
            return getFromFront(index);
        }
        return getFromBack(index);
    }
    private T getFromFront(int i) {
        LinkedList cur = sentinel.next;
        for (int j = 0; j < i; j++) {
            cur = cur.next;
        }
        return (T) cur.t;
    }
    private T getFromBack(int i) {
        LinkedList cur = sentinel.last;
        for (int j = size-1; j > i; j--) {
            cur = cur.last;
        }
        return (T) cur.t;
    }
    public T getRecursive(int index) {
        if (index < size / 2) {
            return (T) getFromFrontR(index, sentinel.next);
        }
        return (T) getFromBackR(index, sentinel.last);
    }
    private T getFromFrontR(int i, LinkedList cur) {
        if (i == 0) {
            return (T) cur.t;
        }
        return (T) getFromFrontR(i-1, cur.next);
    }
    private T getFromBackR(int i, LinkedList cur) {
        if (i == size-1) {
            return (T) cur.t;
        }
        return (T) getFromBackR(i+1, cur.last);
    }
    public void printDeque() {
        LinkedList cur;
        for (cur = sentinel.next; cur.next != sentinel; cur = cur.next) {
            System.out.print(cur.t + " ");
        }
        System.out.println(cur.t);
    }
//    public Iterator<Item> iterator() {
//
//    }
//    public boolean equals(Object o) {
//
//    }
}

//public Iterator<T> iterator(): The Deque objects we’ll make are iterable (i.e. Iterable<T>)
//so we must provide this method to return an iterator.

//public boolean equals(Object o): Returns whether the parameter o is equal to the Deque.
//o is considered equal if it is a Deque and if it contains the same contents
//(as governed by the generic T’s equals method) in the same order.
//(ADDED 2/12: You’ll need to use the instance of keywords for this. Read here for more information)
