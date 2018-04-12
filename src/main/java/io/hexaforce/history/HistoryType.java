package io.hexaforce.history;

/**
 * @author tantaka
 *
 */
public enum HistoryType {
	
	Undetermined("Undetermined"),
    Bounce("Bounce"),
    Complaint("Complaint"),
    Delivery("Delivery");

    private String value;

    private HistoryType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    /**
     * Use this in place of valueOf.
     *
     * @param value
     *        real value
     * @return HistoryType corresponding to the value
     *
     * @throws IllegalArgumentException
     *         If the specified value does not map to one of the known values in this enum.
     */
    public static HistoryType fromValue(String value) {
        if (value == null || "".equals(value)) {
            throw new IllegalArgumentException("Value cannot be null or empty!");
        }

        for (HistoryType enumEntry : HistoryType.values()) {
            if (enumEntry.toString().equals(value)) {
                return enumEntry;
            }
        }

        throw new IllegalArgumentException("Cannot create enum from " + value + " value!");
    }
}
