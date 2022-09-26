package deque;

import java.util.Iterator;
/** A deque built on top of an array */
public class ArrayDeque<T> implements Iterable {
    private class ArrayDequeIterator<T> implements Iterator<T> {
        int pos = startIndex;
        public boolean hasNext() {
            return pos == endIndex;
        }
        public T next() {
            T returnItem = (T) items[pos];
            if (pos == items.length - 1) {
                pos = -1;
            }
            pos++;
            return returnItem;
        }
    }
    private int startIndex = 0, endIndex = 0, size = 0;
    public T[] items = (T[]) new Object[8];

    /** Create an empty deque. */
    public ArrayDeque() {
    }
    /** Move the current items array to a bigger or smaller array as needed. */
    private void changeArraySize(T[] tmp) {
        if (endIndex > startIndex) {
            System.arraycopy(items, startIndex, tmp, 0, size);
        } else {
            System.arraycopy(items, startIndex, tmp, 0, items.length-startIndex);
            System.arraycopy(items, 0, tmp, items.length-startIndex, endIndex);
        }
        startIndex = 0;
        endIndex = size;
        items = tmp;
    }
    /** Add an item to the deque when it is empty, i.e. size == 0. */
    private void addFirstItem(T t) {
        items[0] = t;
        startIndex = 0;
        endIndex = 1;
    }
    /** Add an item to the beginning of the deque. */
    public void addFirst(T t) {
        if (size == items.length) {
            changeArraySize((T[]) new Object[size*2]);
        }
        if (size == 0) {
            addFirstItem(t);
        } else if (startIndex == 0) {
            startIndex = items.length-1;
            items[startIndex] = t;
        } else {
            startIndex--;
            items[startIndex] = t;
        }
        size++;
    }
    /** Remove the first item from the deque. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        int length = items.length;
        if (length > 8 && (size - 1.0) / length < 0.25) {
            changeArraySize((T[]) new Object[length/2]);
        }
        T returnItem = items[startIndex];
        items[startIndex] = null;
        size--;
        if (size != 0) {
            startIndex++;
            if (startIndex == items.length) {
                startIndex = 0;
            }
        }
        return returnItem;
    }
    /** Add an item to the end of the deque. */
    public void addLast(T t) {
        if (size == items.length) {
            changeArraySize((T[]) new Object[size*2]);
        }
        if (size == 0) {
            addFirstItem(t);
        } else if (endIndex == items.length) {
            endIndex = 1;
            items[endIndex-1] = t;
        } else {
            items[endIndex] = t;
            endIndex++;
        }
        size++;
    }
    /** Remove the last item from the deque. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        int length = items.length;
        if (length > 8 && (size - 1.0) / length < 0.25) {
            changeArraySize((T[]) new Object[length/2]);
        }
        if (endIndex == 0) {
            endIndex = length;
        }
        T returnItem = items[endIndex-1];
        items[endIndex-1] = null;
        size--;
        endIndex--;
        return returnItem;
    }
    /** Return true if the deque is empty. */
    public boolean isEmpty() {
        return size == 0;
    }
    /** Return the integer size of the deque. */
    public int size() {
        return size;
    }
    /** Get an item from the deque at a particular index. */
    public T get(int index) {
        if (startIndex >= endIndex) {
            index += startIndex;
            if (index >= items.length) {
                index -= items.length;
            }
        }
        return (T) items[index];
    }
    /** Print the deque in a human-readable format. */
    public void printDeque() {
        if (size == 0) {
            return;
        }
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
    public Iterator<T> iterator() {
        return new ArrayDequeIterator<>();
    }
//    public boolean equals(Object o) {
//        if (!(o instanceof ArrayDeque)) {
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
