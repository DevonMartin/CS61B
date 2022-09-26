package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     */
    public void addIsEmptySizeTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", ad1.isEmpty());
        ad1.addFirst(0);

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad1.size());
        assertFalse("ad1 should now contain 1 item", ad1.isEmpty());

        ad1.addLast(1);
        assertEquals(2, ad1.size());

        ad1.addLast(2);
        assertEquals(3, ad1.size());

        System.out.println("Printing out deque: ");
        ad1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // should be empty
        assertTrue("ad1 should be empty upon initialization", ad1.isEmpty());

        ad1.addFirst(10);
        // should not be empty
        assertFalse("ad1 should contain 1 item", ad1.isEmpty());

        ad1.removeFirst();
        // should be empty
        assertTrue("ad1 should be empty after removal", ad1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(3);

        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();

        int size = ad1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }
    @Test
    /* Check if you can create ArrayDeques with different parameterized types*/
    public void multipleParamTest() {

        ArrayDeque<String>  ad1 = new ArrayDeque<>();
        ArrayDeque<Double>  ad2 = new ArrayDeque<>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

        String s = ad1.removeFirst();
        double d = ad2.removeFirst();
        boolean b = ad3.removeFirst();
    }
    @Test
    /* check if null is return when removing from an empty ArrayDeque. */
    public void emptyNullReturnTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, ad1.removeLast());
    }
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigADequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 1000000; i++) {
            ad1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) ad1.removeLast(), 0.0);
        }
    }
    @Test
    /* Add large number of elements to deque; check if .get works correctly throughout the deque. */
    public void getDequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        ad1.addFirst(5);
        int expected = 5;
        int actual = ad1.get(0);
        assertEquals("Should have the same value", expected, actual);

        ad1.removeFirst();
        for (int i = 0; i <= 5000; i++) {
            ad1.addLast(5000-i);
        }
        for (int i = 0; i <= 5000; i++) {
            actual = ad1.get(i);
            assertEquals("Should have the same value", 5000-i, actual);
        }

        ArrayDeque<Integer> ad2 = new ArrayDeque<>();

        ad2.addLast(0);
        ad2.addLast(1);
        assertEquals((int) ad2.removeFirst(), 0);
        assertEquals((int) ad2.get(0), 1);
    }
    @Test
    /** Ensure list grows and shrinks when appropriate. */
    public void listGrows() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            ad1.addLast(i);
        }
        for (int i = 0; i < 10; i++) {
            ad1.addFirst(i);
        }

        assertEquals("Should have the same value", 4.0, (double) ad1.get(5), 0.0);

        int size = ad1.size();
        for (int i = 0; i < 10; i++) {
            int actual = ad1.removeFirst();
            assertEquals(9-i, actual);
        }
        for (int i = 0; i < 10; i++) {
            int actual = ad1.removeFirst();
            assertEquals(i, actual);
        }

        assertTrue("Deque should be empty after removing all items.", ad1.isEmpty());
    }
    @Test
    public void zeroFourZeroFourDequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        for (int i = 0; i < 4; i++) {
            ad1.addLast(i);
        }
        assertEquals("Should have the same value", 4, ad1.size());
        for (int i = 0; i < 4; i++) {
            ad1.removeLast();
        }
        assertEquals("Should have the same value", 0, ad1.size());
        for (int i = 0; i < 4; i++) {
            ad1.addLast(i);
        }
        assertEquals("Should have the same value", 4, ad1.size());
    }
    @Test
    public void backAndForthAddAndDeleteDequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        ad1.addFirst(0);
        ad1.addLast(0);
        ad1.addLast(1);
        ad1.removeFirst();
        for (int i = 0; i < 10; i++) {
            ad1.addLast(i+2);
        }
    }
    @Test
    public void randomAddAndSubTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        int size = ad1.size();

        for (int i = 0; i < 50000000; i++) {
            boolean rnd = StdRandom.bernoulli(0.5);
            if (rnd) {
                rnd = StdRandom.bernoulli();
                if (rnd) {
                    ad1.addFirst(i);
                } else {
                    ad1.addLast(i);
                }
                size++;
            } else {
                rnd = StdRandom.bernoulli();
                if (rnd) {
                    ad1.removeFirst();
                } else {
                    ad1.removeLast();
                }
                size = Math.max(size-1, 0);
            }
            assertEquals(size, ad1.size());
        }
    }
    @Test
    public void iteratorTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
        int i = 0;
        for (Object item : ad1) {
            assertEquals((int) item, i);
            i++;
        }
    }
    @Test
    public void equalsTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.addFirst(0);
        assertTrue(ad1.equals(lld1));
        assertTrue(lld1.equals(ad1));
    }
}
