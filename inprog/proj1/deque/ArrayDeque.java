package deque;

public class ArrayDeque<T> {
    int startIndex;
    int endIndex;
    int size;
    T[] items;

    public ArrayDeque() {
        startIndex = 0;
        endIndex = 0;
        size = 0;
        items = (T[]) new Object[8];
    }
    private void changeArraySize(T[] tmp) {
        if (endIndex > startIndex) {
            System.arraycopy(items, startIndex, tmp, startIndex, size);
        } else {
            System.arraycopy(items, startIndex, tmp, 0, size-endIndex);
            System.arraycopy(items, 0, tmp, size-endIndex, endIndex);
            startIndex = 0;
            endIndex = size;
        }
        items = tmp;
    }
    public void addFirst(T t) {
        if (size == items.length) {
            changeArraySize((T[]) new Object[size*2]);
        }
        if (startIndex == endIndex) {
            items[0] = t;
            endIndex++;
        } else if (startIndex == 0) {
            startIndex = items.length-1;
            items[startIndex] = t;
        } else {
            startIndex--;
            items[startIndex] = t;
        }
        size++;
    }
//    public T removeFirst() {
//
//    }
    public void addLast(T t) {
        if (size == items.length) {
            changeArraySize((T[]) new Object[size*2]);
        }
        if (endIndex == items.length) {
            endIndex = 0;
            items[endIndex] = t;
        } else {
            items[endIndex] = t;
            endIndex++;
        }
        size++;
    }
//    public T removeLast() {
//
//    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public T get(int index) {
        if (startIndex >= endIndex) {
            index += startIndex;
            if (index >= items.length) {
                index -= items.length;
            }
        }
        return (T) items[index];
    }
    public void printDeque() {
        int i = startIndex;
        int length = items.length;
        while (true) {
            System.out.print(items[i]);
            if (i == endIndex-1) {
                System.out.println();
                return;
            } else {
                System.out.print(" ");
            }
            i++;
            if (i == length) {
                i = 0;
            }
        }
    }
//    public Iterator<Item> iterator() {
//
//    }
//    public boolean equals(Object o) {
//
//    }
}

//public void addFirst(T item): Adds an item of type T to the front of the deque.
//You can assume that item is never null.

//public void addLast(T item): Adds an item of type T to the back of the deque.
//You can assume that item is never null.

//public T removeFirst(): Removes and returns the item at the front of the deque.
//If no such item exists, returns null.

//public T removeLast(): Removes and returns the item at the back of the deque.
//If no such item exists, returns null.

//public Iterator<T> iterator(): The Deque objects we’ll make are iterable (i.e. Iterable<T>)
//so we must provide this method to return an iterator.

//public boolean equals(Object o): Returns whether the parameter o is equal to the Deque.
//o is considered equal if it is a Deque and if it contains the same contents
//(as governed by the generic T’s equals method) in the same order.
//(ADDED 2/12: You’ll need to use the instance of keywords for this. Read here for more information)
