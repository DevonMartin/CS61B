package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B {

    private class BSTNode {
        private K key;
        private V value;
        private BSTNode left = null;
        private BSTNode right = null;
        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public V get(K key) {
            int comp = key.compareTo(this.key);
            if (comp == 0) {
                return value;
            } else if (comp < 0) {
                if (left != null) {
                    return left.get(key);
                }
            } else {
                if (right != null) {
                    return right.get(key);
                }
            }
            return null;
        }
        public void put(K key, V value) {

        }
    }

    private BSTNode root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    public BSTMap(K key, V value) {
        root = new BSTNode(key, value);
        size = 1;
    }

    public boolean hasRoot() {
        return root != null;
    }

    public void printInOrder() {
        printInOrder(this.root);
    }

    private void printInOrder(BSTNode node) {
        if (node != null) {
            printInOrder(node.left);
            System.out.println("Key: " + node.key);
            System.out.println("Value: " + node.value);
            printInOrder(node.right);
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public V get(Object key) {
        if (root != null) {
            return root.get((K) key);
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(Object key, Object value) {
        if (root != null) {
            root.put((K) key, (V) value);
        } else {
            root = new BSTNode((K) key, (V) value);
        }
    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public Object remove(Object key, Object value) {
        return null;
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
