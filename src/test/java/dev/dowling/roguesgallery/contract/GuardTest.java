package dev.dowling.roguesgallery.contract;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuardTest {

    @Test
    public void AgainstNegative_givenNegativeNumber_ThrowsException() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> Guard.AgainstNegative(-17),
                "Expected Guard.AgainstNegative to throw");

        assertTrue(thrown.getMessage().contains("-17"));
    }

    @Test
    public void AgainstNegative_givenZeroOrPositiveValue_doesNotThrow() {
        Guard.AgainstNegative(0);
        Guard.AgainstNegative(42);
    }

    @Test
    public void AgainstNull_givenNull_throwsException() {
        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> Guard.AgainstNull(null),
                "Expected Guard.AgainstNull to throw");

        assertTrue(thrown.getMessage().contains("cannot be null"));
    }

    @Test
    public void AgainstNull_givenNonNull_doesNotThrow() {
        Guard.AgainstNull(this);
    }

    @Test
    public void AgainstZero_givenZero_throwsException() {
        IllegalArgumentException thrown = assertThrows(
          IllegalArgumentException.class,
                () -> Guard.AgainstZero(0)
        );

        assertTrue(thrown.getMessage().contains("zero"));
    }

    @Test
    public void AgainstZero_givenNotZero_doesNotThrow() {
        Guard.AgainstZero(-54);
        Guard.AgainstZero(77);
    }

}