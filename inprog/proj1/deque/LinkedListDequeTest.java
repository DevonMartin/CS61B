package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results. */
    @Test
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    @Test
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }
    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }
    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        assertNull("Should return null when removeFirst is called on an empty Deque,", lld1.removeFirst());
        assertNull("Should return null when removeLast is called on an empty Deque,", lld1.removeLast());

    }
    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }
	@Test
	/* Add large number of elements to deque; check if .get works correctly throughout the deque. */
	public void getDequeTest() {
		LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

		lld1.addFirst(5);
		int expected = 5;
		int actual = lld1.get(0);
		assertEquals("Should have the same value", expected, actual);

		lld1.removeFirst();
		for (int i = 0; i <= 5000; i++) {
			lld1.addLast(5000-i);
		}
		for (int i = 0; i <= 5000; i++) {
			actual = lld1.get(i);
            assertEquals("Should have the same value", 5000-i, actual);
		}
	}
    @Test
    /* Add large number of elements to deque; check if .get works correctly throughout the deque. */
    public void getRDequeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        lld1.addFirst(5);
        int expected = 5;
        int actual = lld1.getRecursive(0);
        assertEquals("Should have the same value", expected, actual);

        lld1.removeFirst();
        for (int i = 0; i <= 5000; i++) {
            lld1.addLast(5000 - i);
        }
        for (int i = 0; i <= 5000; i++) {
            actual = lld1.getRecursive(i);
            assertEquals("Should have the same value", 5000 - i, actual);
        }
    }
    @Test
    public void zeroFourZeroFourDequeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        for (int i = 0; i < 4; i++) {
            lld1.addLast(i);
        }
        assertEquals("Should have the same value", 4, lld1.size());
        for (int i = 0; i < 4; i++) {
            lld1.removeFirst();
        }
        assertEquals("Should have the same value", 0, lld1.size());
        for (int i = 0; i < 4; i++) {
            lld1.addLast(i);
        }
        assertEquals("Should have the same value", 4, lld1.size());
    }
    @Test
    public void backAndForthAddAndDeleteDequeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        lld1.addFirst(0);
        lld1.addLast(0);
        lld1.addLast(1);
        lld1.removeFirst();
        for (int i = 0; i < 10; i++) {
            lld1.addLast(i+2);
        }
    }
    @Test
    public void randomAddAndSubTest() {
        LinkedListDeque<Integer> ad1 = new LinkedListDeque<>();
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
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.addFirst(0);
        int i = 0;
        for (Object item : lld1) {
            assertEquals((int) item, i);
            i++;
        }
    }
    @Test
    public void equalsTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        lld1.addFirst(3);
        lld1.addFirst(2);
        lld1.addFirst(1);
        lld1.addFirst(0);
        ad1.addFirst(3);
        ad1.addFirst(2);
        ad1.addFirst(1);
        ad1.addFirst(0);
        assertEquals(lld1, ad1);
        assertEquals(ad1, lld1);
    }
}