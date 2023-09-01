package sv.app.javaee.bandesal.util;

/**
 * Enumeracion de ordenamiento.
 * 
 * @author Raul Garcia
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum SortOrder {

    ASCENDING(1, "asc", "1", "ascending", "ASCENDING", "ASC"),
    DESCENDING(-1, "desc", "-1", "descending", "DESCENDING", "DESC"),
    UNSORTED(0, "", null, "0", "unsorted", "UNSORTED");

    private final Set<Object> values;

    private final Integer intValue;

    SortOrder(int intValue, Object... values) {
        this.intValue = intValue;
        this.values = new HashSet<>(Arrays.asList(values));
    }

    public static SortOrder of(Object order) {
        for (SortOrder o : values()) {
            if (o.intValue.equals(order) || o.values.contains(order)) {
                return o;
            }
        }

        throw new IllegalArgumentException("No SorderOrder matching value: " + order);
    }

    public boolean isAscending() {
        return this == SortOrder.ASCENDING;
    }

    public boolean isDescending() {
        return this == SortOrder.DESCENDING;
    }

    public boolean isUnsorted() {
        return this == SortOrder.UNSORTED;
    }

    public int intValue() {
        return intValue;
    }
}
