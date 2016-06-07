package testing;

import org.junit.Before;
import org.junit.Test;
import structures.AVLSet;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AVLSetTest {

    private static final int RANDOMS = 10000; // total de número aleatorios a generar.
    private AVLSet<Integer> set = new AVLSet<Integer>();
    private Random rand = new Random();
    private TreeSet<Integer> inserted = new TreeSet<Integer>();

    @Before public void setUp () {
        inserted.clear();
        for (int i = 0; i < RANDOMS; i++) {
            int r = rand.nextInt(RANDOMS);
            set.add(r);
            inserted.add(r);
        }
    }

    @Test public void higherIteratorTest () {
        int higherThan = RANDOMS / 2;
        Iterator<Integer> higherIter = set.higherIterator(higherThan);
        Set<Integer> higherSet = inserted.tailSet(higherThan, true);
        while (higherIter.hasNext()) {
            int higher = higherIter.next();
            assertTrue(higher >= higherThan); // cumple con ser mayor
            assertTrue(higherSet.contains(higher)); // cumple en estar entre los mayores
        }
    }

    @Test public void inorderIteratorTest () {
        int prev = -1; // solo hay valores positivos en el set
        int cant = 0;
        for (Integer i : set) {
            assertTrue(i > prev);
            prev = i;
            cant++;
        }
        assertTrue(cant == set.size());
    }

    @Test public void clearTest () {
        set.clear();
        assertTrue(set.size() == 0);
        for (Integer n : inserted)
            assertFalse(set.contains(n));
        setUp();
        treeTest();
    }

    @Test public void treeTest () {
        for (Integer i : inserted)
            assertTrue(set.contains(i));
        assertTrue(set.containsAll(inserted));
        assertTrue(set.containsAll(set));
        assertTrue(set.size() == inserted.size());

        for (int i = 0; i < RANDOMS; i++) {
            int r = rand.nextInt(RANDOMS);
            set.remove(r);
            inserted.remove(r);
        }

        for (Integer i : inserted)
            assertTrue(set.contains(i));
        assertTrue(set.containsAll(inserted));
        assertTrue(set.containsAll(set));
        assertTrue(set.size() == inserted.size());
    }

    @Test public void mergeTest () {
        set = set.merge(set); // no debería cambiar
        treeTest();

        AVLSet<Integer> negativeSet = new AVLSet<Integer>();
        for (int i = 0; i < RANDOMS; i++) {
            int r = rand.nextInt(RANDOMS);
            negativeSet.add(-r - 1); // nos aseguramos con el -1 que no pueda dar cero
        }

        AVLSet<Integer> merged = set.merge(negativeSet);
        assertTrue(merged.size() == set.size() + negativeSet.size());
        merged.containsAll(set);
        merged.containsAll(negativeSet);
        // TODO: probar más cosas

    }
}
