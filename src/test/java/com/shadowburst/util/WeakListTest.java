package com.shadowburst.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(org.junit.runners.JUnit4.class)
public class WeakListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructors() {
        final WeakList<Object> def = new WeakList<Object>();
        final WeakList<Object> four = new WeakList<Object>(4);
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
        final Integer i = 3;
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
        final WeakList<Integer> is = new WeakList<Integer>(1);

        thrown.expect(NullPointerException.class);
        is.set(0, null);
    }
}
