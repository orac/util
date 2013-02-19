package com.shadowburst.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Collections;
import java.util.List;
import org.junit.rules.ExpectedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(org.junit.runners.JUnit4.class)
public class WeakListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructors() {
        WeakList<Object> def = new WeakList<Object>();
        WeakList<Object> four = new WeakList<Object>(4);
        assertTrue(def.isEmpty());
        assertTrue(four.isEmpty());
        assertEquals(0, def.size());
        assertEquals(0, four.size());
    }

    @Test
    public void emptyGetOOB() throws IndexOutOfBoundsException {
        WeakList<Object> def = new WeakList<Object>();
        thrown.expect(IndexOutOfBoundsException.class);
        def.get(0);
    }

    @Test
    public void singleton() {
        WeakList<Integer> is = new WeakList<Integer>(1);
        Integer i = new Integer(3);
        is.add(i);
        assertFalse(is.isEmpty());
        assertTrue(is.contains(i));
        {
            List<Integer> expected = Collections.singletonList(i);
            assertTrue(is.containsAll(expected));
        }
        {
            Integer expected[] = new Integer[] { i };
            Object result[] = is.toArray();
            assertArrayEquals(expected, result);
        }
        assertEquals(0, is.indexOf(i));
    }

    @Test
    public void setNull() throws NullPointerException {
        WeakList<Integer> is = new WeakList<Integer>(1);
        Integer i = new Integer(3);

        thrown.expect(NullPointerException.class);
        is.set(0, null);
    }
}
