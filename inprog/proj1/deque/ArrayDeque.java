package deque;

import java.util.Iterator;

public class ArrayDeque<T> {
    private int startIndex;
    private int endIndex;
    private int size;
    private T[] items;

    public ArrayDeque() {
        startIndex = 0;
        endIndex = 0;
        size = 0;
        items = (T[]) new Object[8];
    }
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
    private void addFirstItem(T t) {
        items[0] = t;
        startIndex = 0;
        endIndex = 1;
    }
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
//    public Iterator<T> iterator() {
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
