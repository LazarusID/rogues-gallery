package dev.dowling.roguesgallery.action;

import dev.dowling.roguesgallery.domain.AppUser;
import dev.dowling.roguesgallery.entity.CampaignEntity;
import dev.dowling.roguesgallery.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

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

    @Test
    public void call_whenGetterReturnsEmptyOptiuonal_returnsEmptyUser() throws Exception {
        var actual = new GetAppUser(6, GetAppUserTest::dummyGetter).call();
        assertEquals(AppUser.emptyUser, actual);
    }

    @Test
    public void call_afterWithCampaignscalled_returnsPopulatedCampaignList() throws Exception {
        var actual = new GetAppUser(6, GetAppUserTest::getterWithCampaigns)
                .withCampaigns()
                .call();

        assertNotNull(actual.getCampaigns());
        assertEquals(2, actual.getCampaigns().size());

        var campaigns = actual.getCampaigns();

        assertTrue(campaigns.stream().anyMatch(c -> c.getName() == "first"));
        assertTrue(campaigns.stream().anyMatch(c -> c.getName() == "second"));
    }

    @Test
    public void call_withoutWithCampaigns_returnsNullCampaignList() throws Exception {
        var actual = new GetAppUser(6, GetAppUserTest::getterWithCampaigns)
                .call();

        assertNull(actual.getCampaigns());
    }

    private static Optional<UserEntity> dummyGetter(Integer in) {
        return Optional.empty();
    }

    private static Optional<UserEntity> getterWithCampaigns(Integer i) {
        var e = new UserEntity();
        e.setName("User Six");
        e.setEmail("six@example.com");
        e.setId(i);

        var c1 = new CampaignEntity(i, "first", true);
        c1.setId(78);
        var c2 = new CampaignEntity(i, "second", true);
        c2.setId(79);
        e.setCampaigns(Set.of(c1, c2));

        return Optional.of(e);
    }

}