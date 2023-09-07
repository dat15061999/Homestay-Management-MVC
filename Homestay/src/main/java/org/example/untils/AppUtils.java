package org.example.untils;

import java.util.List;

public class AppUtils {
    public static int findNext(List<Integer> integers) {
        int max = 0;
        for (Integer integer : integers) {
            if (integer > max) {
                max = integer;
            }
        }
        return ++max;
    }
    public static long findNext2(List<Long> integers) {
        long max = 0;
        for (Long integer : integers) {
            if (integer > max) {
                max = integer;
            }
        }
        return ++max;
    }
}
