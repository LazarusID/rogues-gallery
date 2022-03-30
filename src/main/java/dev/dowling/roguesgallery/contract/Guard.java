package dev.dowling.roguesgallery.contract;

public class Guard {

    public static void AgainstNegative(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException(String.format("Argument (%d) cannot be negative", value));
        }
    }

    public static void AgainstNull(final Object value) {
        if (null == value) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
    }

    public static void AgainstZero(final int value) {
        if (0 == value) {
            throw new IllegalArgumentException(("Argument cannot be zero"));
        }
    }



}
