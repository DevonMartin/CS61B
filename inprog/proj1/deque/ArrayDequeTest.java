package deque;

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {
    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     */
    public void addIsEmptySizeTest() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

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
    /** Adds 9 items to the list to check if it grows. */
    public void listGrows() {

        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        for (int i = 0; i < 20; i++) {
            ad1.addLast(i);
        }
        ad1.printDeque();
    }
}
