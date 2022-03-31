package dev.dowling.roguesgallery.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder
@Data
public class AppUser {

    public static final AppUser emptyUser = new AppUser(-1, "EMPTY", "EMPTY", null);

    private final int id;
    private final String email;
    private final String name;

    private final List<Campaign> campaigns;

}
