package dev.dowling.roguesgallery.action;

import dev.dowling.roguesgallery.domain.AppUser;
import dev.dowling.roguesgallery.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GetAppUserTest {

    @Test
    public void call_passesUserIdToGetter() throws Exception {

        var sut = new GetAppUser(7, (Integer in) -> {return Optional.of(new UserEntity(in, "user@example.org", "User"));});

        var actual = sut.call();

        assertEquals(7, actual.getId());
        assertEquals("user@example.org", actual.getEmail());
        assertEquals("User", actual.getName());
    }

    @Test
    public void constr_givenNullnegativeOrZeroUserId_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> { var a = new GetAppUser(null, GetAppUserTest::dummyGetter); },
                "Expected IllegalArgumentExceptoin"
        );
        assertThrows(IllegalArgumentException.class,
                () -> { var a = new GetAppUser(-99, GetAppUserTest::dummyGetter); },
                "Expected IllegalArgumentExceptoin"
        );
        assertThrows(IllegalArgumentException.class,
                () -> { var a = new GetAppUser(0, GetAppUserTest::dummyGetter); },
                "Expected IllegalArgumentExceptoin"
        );
    }

    @Test
    public void constr_givenNullGetter_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> { var a = new GetAppUser(299, null); },
                "Expected IllegalArgumentExceptoin"
        );
    }

    private static Optional<UserEntity> dummyGetter(Integer in) {
        return Optional.empty();
    }

}