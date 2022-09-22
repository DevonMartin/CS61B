package deque;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.ThreadLocalRandom;

public class MaxArrayDequeTest {
    @Test
    public void comparatorTest() {
        for (int i = 0; i < 500; i++) {
            int maxInt = ThreadLocalRandom.current().nextInt(2, 400000);
            MaxIntUnder<Integer> c1 = new MaxIntUnder<>(maxInt);
            MaxArrayDeque mad1 = new MaxArrayDeque(c1);
            int k = ThreadLocalRandom.current().nextInt(2, 400000);
            for (int j = 0; j <= k; j++) {
                mad1.addLast(j);
            }
            assertEquals("Expected values to be equal: ", Math.min(k, maxInt), mad1.max());
        }
    }
}
