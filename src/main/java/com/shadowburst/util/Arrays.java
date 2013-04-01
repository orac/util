package com.shadowburst.util;

/** Utilities for common operations on arrays.
 * Use me like (or in conjunction with) #java.util.Arrays .
 */
public abstract class Arrays {
    private Arrays() { }

    public static boolean contains(int[] array, int needle) {
        for (int i : array) {
            if (i == needle)
                return true;
        }
        return false;
    }
}
