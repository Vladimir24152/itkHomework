package org.neoflex;

import java.util.HashMap;
import java.util.Map;

public class MapperFromArrayToMap {
    public static <T> Map<T, Integer> mapFromArrayToMap(T[] array) {
        Map<T, Integer> result = new HashMap<>();
        if (array == null) {
            return result;
        }
        for (T item : array) {
            result.merge(item, 1, Integer::sum);
        }
        return result;
    }
}
