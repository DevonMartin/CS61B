package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> al1 = new AListNoResizing<>();
        BuggyAList<Integer> al2 = new BuggyAList<>();
        for (int i = 0; i < 1000; i++) {
            al1.addLast(i);
            al2.addLast(i);
        }
        for (int i = 0; i < 1000; i++) {
            assertEquals(al1.removeLast(), al2.removeLast());
        }
    }
}
