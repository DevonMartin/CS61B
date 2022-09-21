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
            System.arraycopy(items, startIndex, tmp, 0, size);
        } else {
            System.arraycopy(items, startIndex, tmp, 0, size-endIndex-1);
            System.arraycopy(items, 0, tmp, size-endIndex, endIndex+1);
        }
        startIndex = 0;
        endIndex = size;
        items = tmp;
    }
    public void addFirst(T t) {
        if (size == items.length) {
            changeArraySize((T[]) new Object[size*2]);
        }
        if (size == 0) {
            items[0] = t;
            startIndex = 0;
            endIndex = 1;
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
        }
        if (startIndex == items.length) {
            startIndex = 0;
        }
        return returnItem;
    }
    public void addLast(T t) {
        if (size == items.length) {
            changeArraySize((T[]) new Object[size*2]);
        }
        if (size == 0) {
            items[0] = t;
            startIndex = 0;
            endIndex = 1;
        } else if (endIndex == items.length) {
            endIndex = 0;
            items[endIndex] = t;
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
        if ((size - 1.0) / length < 0.25) {
            changeArraySize((T[]) new Object[length/2]);
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
//    public Iterator<Item> iterator() {
//
//    }
//    public boolean equals(Object o) {
//
//    }
}

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
