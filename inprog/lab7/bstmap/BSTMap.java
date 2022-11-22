package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable, V> implements Map61B {

    private class BSTNode {
        private K key;
        private V value;
        BSTNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private BSTNode root;
    private int size;
    private BSTMap left;
    private BSTMap right;

    public BSTMap() {
        root = null;
        size = 0;
        left = null;
        right = null;
    }

    public void printInOrder() {
        printInOrder(this);
    }

    private static void printInOrder(BSTMap tree) {
        if (tree != null) {
            printInOrder(tree.left);
            System.out.println("Key: " + tree.root.key);
            System.out.println("Value: " + tree.root.value);
            printInOrder(tree.right);
        }
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
        left = null;
        right = null;
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public V get(Object key) {
        return get((K) key, this);
    }

    private V get(K key, BSTMap tree) {
        if (tree == null || tree.root == null) {
            return null;
        }
        BSTNode node = tree.root;
        int comp = key.compareTo(node.key);
        if (comp == 0) {
            return node.value;
        } else if (comp < 0) {
            return get(key, tree.left);
        } else {
            return get(key, tree.right);
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(Object key, Object value) {

    }

    private void put(K key, V value) {

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
