package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class LinkedListDequeIterator implements Iterator<T> {
        LinkedList<T> cur = sentinel.next;
        @Override
        public boolean hasNext() {
            return cur != sentinel;
        }

        @Override
        public T next() {
            T returnItem = cur.t;
            cur = cur.next;
            return returnItem;
        }
    }
    /** Subclass utilized by LinkedListDeque
     *
     * @param <T> the item stored at the linked list/node.
     */
    private static class LinkedList<T> {
        T t;
        LinkedList<T> next;
        LinkedList<T> last;
        LinkedList(T t) {
            this.t = t;
        }
    }
    private int size = 0;
    private final LinkedList<T> sentinel = new LinkedList(0);

    /** Instantiate a deque with a sentinel node that points at itself. */
    public LinkedListDeque() {
        sentinel.next = sentinel;
        sentinel.last = sentinel;
    }
    /** Add an item to the beginning of the deque. */
    @Override
    public void addFirst(T t) {
        LinkedList<T> tmp = new LinkedList<>(t);
        tmp.next = sentinel.next;
        tmp.next.last = tmp;
        tmp.last = sentinel;
        sentinel.next = tmp;
        size++;
    }
    /** Remove the first item from the deque. */
    @Override
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T returnT = sentinel.next.t;
        sentinel.next = sentinel.next.next;
        sentinel.next.last = sentinel;
        size--;
        return returnT;
    }
    /** Add an item to the end of the deque. */
    @Override
    public void addLast(T t) {
        LinkedList<T> tmp = new LinkedList<>(t);
        tmp.last = sentinel.last;
        tmp.last.next = tmp;
        tmp.next = sentinel;
        sentinel.last = tmp;
        size++;
    }
    /** Remove the last item from the deque. */
    @Override
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T returnT = sentinel.last.t;
        sentinel.last = sentinel.last.last;
        sentinel.last.next = sentinel;
        size--;
        return returnT;
    }
    /** Return the integer size of the deque. */
    @Override
    public int size() {
        return size;
    }
    /** Get an item from a particular location in the deque using iteration. */
    @Override
    public T get(int index) {
        if (index < size / 2) {
            return getFromFront(index);
        }
        return getFromBack(index);
    }
    /** Helper function for get that is used when the index of the item requested
     *  is closest to the front of the deque. */
    private T getFromFront(int i) {
        LinkedList<T> cur = sentinel.next;
        for (int j = 0; j < i; j++) {
            cur = cur.next;
        }
        return cur.t;
    }
    /** Helper function for get that is used when the index of the item requested
     *  is closest to the back of the deque. */
    private T getFromBack(int i) {
        LinkedList<T> cur = sentinel.last;
        for (int j = size - 1; j > i; j--) {
            cur = cur.last;
        }
        return cur.t;
    }
    /** Get an item from a particular location in the deque using iteration. */
    public T getRecursive(int index) {
        if (index < size / 2) {
            return getFromFrontR(index, sentinel.next);
        }
        return getFromBackR(index, sentinel.last);
    }
    /** Helper function for getRecursive that is used when the index of the item
     * requested is closest to the front of the deque. */
    private T getFromFrontR(int i, LinkedList<T> cur) {
        if (i == 0) {
            return cur.t;
        }
        return getFromFrontR(i - 1, cur.next);
    }
    /** Helper function for getRecursive that is used when the index of the item
     * requested is closest to the back of the deque. */
    private T getFromBackR(int i, LinkedList<T> cur) {
        if (i == size - 1) {
            return cur.t;
        }
        return getFromBackR(i + 1, cur.last);
    }
    /** Print the deque in a human-readable format. */
    @Override
    public void printDeque() {
        LinkedList<T> cur;
        for (cur = sentinel.next; cur.next != sentinel; cur = cur.next) {
            System.out.print(cur.t + " ");
        }
        System.out.println(cur.t);
    }
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
    public boolean equals(Object o) {
        if (!(o instanceof Deque) || size != ((Deque<?>) o).size()) {
            return false;
        }
        Iterator<T> iter = iterator();
        int i = 0;
        while (iter.hasNext()) {
            if (!(iter.next().equals(((Deque<?>) o).get(i)))) {
                return false;
            }
            i++;
        }
        return true;
    }
}

//public boolean equals(Object o): Returns whether the parameter o is equal to the Deque.
//o is considered equal if it is a Deque and if it contains the same contents
//(as governed by the generic T’s equals method) in the same order.
//(ADDED 2/12: You’ll need to use the instance of keywords for this. Read here for more information)
