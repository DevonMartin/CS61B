//package deque;
//
//import edu.princeton.cs.algs4.StdRandom;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import java.util.Comparator;
//import java.util.concurrent.ThreadLocalRandom;
//
//public class MaxArrayDequeTest {
//    @Test
//    /* Tests using a comparator to use .max() */
//    public void comparatorTest() {
//        for (int i = 0; i < 500; i++) {
//            int maxInt = ThreadLocalRandom.current().nextInt(2, 400000);
//            Comparator<Integer> c1 = MaxArrayDeque.getMaxIntUnderComparator(maxInt);
//            MaxArrayDeque mad1 = new MaxArrayDeque<>(c1);
//            int k = ThreadLocalRandom.current().nextInt(2, 400000);
//            for (int j = 0; j <= k; j++) {
//                mad1.addLast(j);
//            }
//            assertEquals("Expected values to be equal: ", Math.min(k, maxInt), mad1.max());
//        }
//    }
//    @Test
//    public void randomAddAndSubTest() {
//        Comparator<Integer> c1 = MaxArrayDeque.getMaxIntUnderComparator(100);
//        MaxArrayDeque<Integer> ad1 = new MaxArrayDeque<>(c1);
//        int size = ad1.size();
//
//        for (int i = 0; i < 50000000; i++) {
//            boolean rnd = StdRandom.bernoulli(0.5);
//            if (rnd) {
//                rnd = StdRandom.bernoulli();
//                if (rnd) {
//                    ad1.addFirst(i);
//                } else {
//                    ad1.addLast(i);
//                }
//                size++;
//            } else {
//                rnd = StdRandom.bernoulli();
//                if (rnd) {
//                    ad1.removeFirst();
//                } else {
//                    ad1.removeLast();
//                }
//                size = Math.max(size-1, 0);
//            }
//            assertEquals(size, ad1.size());
//        }
//    }
//}
