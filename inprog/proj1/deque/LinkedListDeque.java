package deque;

public class LinkedListDeque<T> {
    /** Subclass utilized by LinkedListDeque
     *
     * @param <T> the item stored at the linked list/node.
     */
    private class LinkedList<T> {
        T t;
        LinkedList next;
        LinkedList last;
        LinkedList(T t) {
            this.t = t;
        }
    }
    private int size = 0;
    private LinkedList sentinel = new LinkedList(0);

    /** Instantiate a deque with a sentinel node that points at itself. */
    public LinkedListDeque() {
        sentinel.next = sentinel;
        sentinel.last = sentinel;
    }
    /** Add an item to the beginning of the deque. */
    public void addFirst(T t) {
        LinkedList tmp = new LinkedList(t);
        tmp.next = sentinel.next;
        tmp.next.last = tmp;
        tmp.last = sentinel;
        sentinel.next = tmp;
        size++;
    }
    /** Remove the first item from the deque. */
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
    /** Add an item to the end of the deque. */
    public void addLast(T t) {
        LinkedList tmp = new LinkedList(t);
        tmp.last = sentinel.last;
        tmp.last.next = tmp;
        tmp.next = sentinel;
        sentinel.last = tmp;
        size++;
    }
    /** Remove the last item from the deque. */
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
    /** Return true if the deque is empty, i.e. size == 0. */
    public boolean isEmpty() {
        return size == 0;
    }
    /** Return the integer size of the deque. */
    public int size() {
        return size;
    }
    /** Get an item from a particular location in the deque using iteration. */
    public T get(int index) {
        if (index < size / 2) {
            return getFromFront(index);
        }
        return getFromBack(index);
    }
    /** Helper function for get that is used when the index of the item requested
     *  is closest to the front of the deque. */
    private T getFromFront(int i) {
        LinkedList cur = sentinel.next;
        for (int j = 0; j < i; j++) {
            cur = cur.next;
        }
        return (T) cur.t;
    }
    /** Helper function for get that is used when the index of the item requested
     *  is closest to the back of the deque. */
    private T getFromBack(int i) {
        LinkedList cur = sentinel.last;
        for (int j = size-1; j > i; j--) {
            cur = cur.last;
        }
        return (T) cur.t;
    }
    /** Get an item from a particular location in the deque using iteration. */
    public T getRecursive(int index) {
        if (index < size / 2) {
            return (T) getFromFrontR(index, sentinel.next);
        }
        return (T) getFromBackR(index, sentinel.last);
    }
    /** Helper function for getRecursive that is used when the index of the item
     * requested is closest to the front of the deque. */
    private T getFromFrontR(int i, LinkedList cur) {
        if (i == 0) {
            return (T) cur.t;
        }
        return (T) getFromFrontR(i-1, cur.next);
    }
    /** Helper function for getRecursive that is used when the index of the item
     * requested is closest to the back of the deque. */
    private T getFromBackR(int i, LinkedList cur) {
        if (i == size-1) {
            return (T) cur.t;
        }
        return (T) getFromBackR(i+1, cur.last);
    }
    /** Print the deque in a human-readable format. */
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
//        if (!(o instanceof LinkedListDeque)) {
//            return false;
//        }
//    }
}

//public Iterator<T> iterator(): The Deque objects we’ll make are iterable (i.e. Iterable<T>)
//so we must provide this method to return an iterator.

//public boolean equals(Object o): Returns whether the parameter o is equal to the Deque.
//o is considered equal if it is a Deque and if it contains the same contents
//(as governed by the generic T’s equals method) in the same order.
//(ADDED 2/12: You’ll need to use the instance of keywords for this. Read here for more information)
