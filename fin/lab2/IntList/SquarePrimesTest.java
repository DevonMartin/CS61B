package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }
    @Test
    public void testSquarePrimesToF() {
        assertTrue(IntListExercises.squarePrimes(IntList.of(1, 2, 3)));
        assertTrue(IntListExercises.squarePrimes(IntList.of(3)));
        assertFalse(IntListExercises.squarePrimes(IntList.of(8, 10, 12, 15, 25)));
    }
    @Test
    public void testSquarePrimesSquares() {
        IntList l1 = IntList.of(5, 7, 9);
        IntListExercises.squarePrimes(l1);
        assertEquals("25 -> 49 -> 9", l1.toString());
    }
}
