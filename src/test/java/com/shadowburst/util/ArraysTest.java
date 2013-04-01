package com.shadowburst.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class ArraysTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testContains() throws Exception {
        final int[] haystack = {0, -1, Integer.MAX_VALUE, 2 };
        assertTrue(Arrays.contains(haystack, -1));
        assertTrue(Arrays.contains(haystack, 0));
        assertTrue(Arrays.contains(haystack, 2));
        assertTrue(Arrays.contains(haystack, Integer.MAX_VALUE));
        assertFalse(Arrays.contains(haystack, 1));
        assertFalse(Arrays.contains(haystack, Integer.MIN_VALUE));
        assertFalse(Arrays.contains(haystack, 17));
    }
}
