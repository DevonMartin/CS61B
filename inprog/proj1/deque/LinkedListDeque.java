package deque;

public class LinkedListDeque<Item> {
    private class LinkedList<Item> {
        Item item;
        LinkedList next;
        LinkedList last;
        LinkedList(Item item) {
            this.item = item;
            this.next = this;
            this.last = this;
        }
    }
    private int size;
    LinkedList sentinel;

    public LinkedListDeque() {
        sentinel = new LinkedList(0);
        size = 0;
    }
    public void addFirst(Item item) {
        LinkedList tmp = new LinkedList(item);
        tmp.next = sentinel.next;
        tmp.next.last = tmp;
        tmp.last = sentinel;
        sentinel.next = tmp;
        size++;
    }
    public void addLast(Item item) {
        LinkedList tmp = new LinkedList(item);
        tmp.last = sentinel.last;
        tmp.last.next = tmp;
        tmp.next = sentinel;
        sentinel.last = tmp;
        size++;
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public void printDeque() {
        LinkedList cur = sentinel.next;
        int size = this.size;
        while (size > 1) {
            System.out.print(cur.item + " ");
            cur = cur.next;
            size--;
        }
        System.out.println(cur.item);
    }
}

//public T removeFirst(): Removes and returns the item at the front of the deque. If no such item exists, returns null.

//public T removeLast(): Removes and returns the item at the back of the deque. If no such item exists, returns null.

//public T get(int index): Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth. If no such item exists, returns null. Must not alter the deque!

//public T getRecursive(int index): Same as get, but uses recursion.

//public Iterator<T> iterator(): The Deque objects we’ll make are iterable (i.e. Iterable<T>) so we must provide this method to return an iterator.

//public boolean equals(Object o): Returns whether the parameter o is equal to the Deque. o is considered equal if it is a Deque and if it contains the same contents (as goverened by the generic T’s equals method) in the same order. (ADDED 2/12: You’ll need to use the instance of keywords for this. Read here for more information)
