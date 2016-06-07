package testing;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import structures.BinaryMinHeap;

import static org.junit.Assert.*;

public class MinHeapTest {

    private BinaryMinHeap<String> heap;

    @BeforeClass public static void setUpBeforeClass () throws Exception {
    }

    @Before public void setUp () throws Exception {
        heap = new BinaryMinHeap<String>(100);
    }

    @Test public void testCorrectQueuing () {
        String[] strs =
            new String[] {"first", "second", "third", "fourth", "fifth", "sixth", "seventh",
                "eighth", "ninth", "tenth"};

        //Se encolan con prioridad segun el orden que tienen en el array, pero en desorden
        heap.enqueue(strs[7], 1122);
        heap.enqueue(strs[2], 2.0);
        heap.enqueue(strs[9], 149499);
        heap.enqueue(strs[1], 1.3);
        heap.enqueue(strs[3], 10);

        assertTrue(heap.size() == 5);

        heap.enqueue(strs[4], 13.1);
        heap.enqueue(strs[0], 0);
        heap.enqueue(strs[6], 29.33);
        heap.enqueue(strs[5], 14.1);
        heap.enqueue(strs[8], 19459);
        heap.enqueue("last", Double.POSITIVE_INFINITY);

        assertTrue(heap.size() == 11);

        assertFalse(heap.isEmpty());

        for (String s : strs) {
            assertEquals(s, heap.dequeue());
        }

        assertEquals("last", heap.dequeue());

        assertTrue(heap.isEmpty());


        heap.enqueue("D", 4);
        heap.enqueue("B", 2);
        heap.enqueue("A", 1);
        heap.enqueue("C", 3);
        heap.enqueue("E", 5);

        assertEquals(heap.dequeue(), "A");

        heap.enqueue("A", 1);

        assertEquals(heap.dequeue(), "A");

    }


    @Test public void testDecreasePriority () {
        heap.enqueue("A", 10);
        heap.enqueue("B", 20);
        heap.enqueue("C", 30);
        heap.enqueue("D", 40);
        heap.enqueue("E", 50);
        heap.enqueue("F", 60);
        heap.enqueue("G", 70);
        heap.enqueue("H", 80);


        assertTrue(heap.getPriority("A") == 10);
        heap.decreasePriority("A", 5);
        assertTrue(heap.getPriority("A") == 5);

        heap.decreasePriority("G", 1);

        assertEquals(heap.dequeue(), "G");

        heap.decreasePriority("D", 6);
        heap.decreasePriority("E", 15);

        //A = 5, D = 6, E = 15, B = 20
        assertEquals(heap.dequeue(), "A");
        assertEquals(heap.dequeue(), "D");
        assertEquals(heap.dequeue(), "E");
        assertEquals(heap.dequeue(), "B");

    }

    @Test(expected = IllegalArgumentException.class) public void test () {
        heap.enqueue("A", 10);
        heap.enqueue("B", 20);
        heap.enqueue("C", 30);

        //Debe ser menor o igual
        heap.decreasePriority("A", 25);
    }

}
