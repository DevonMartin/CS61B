package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void randomAddAndSubTest() {
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        String errmsg = "";

        for (int i = 0; i < 5000; i++) {
            boolean rnd = StdRandom.bernoulli();
            if (rnd) {
                rnd = StdRandom.bernoulli();
                if (rnd) {
                    errmsg += "addFirst(" + i + ")\n";
                    sad.addFirst(i);
                    ads.addFirst(i);
                } else {
                    errmsg += "addLast(" + i + ")\n";
                    sad.addLast(i);
                    ads.addLast(i);
                }
            } else if (sad.size() != 0 && ads.size() != 0) {
                rnd = StdRandom.bernoulli();
                if (rnd) {
                    errmsg += "removeFirst()\n";
                    assertEquals(errmsg, sad.removeFirst(), ads.removeFirst());
                } else {
                    errmsg += "removeLast()\n";
                    assertEquals(errmsg, sad.removeLast(), ads.removeLast());
                }
            }
        }
    }
}
