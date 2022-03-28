package dev.dowling.roguesgallery.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder
@Data
public class AppUser {
    @NonNull
    private final int id;
    @NonNull
    private final String email;
    private final String name;

    private final List<Campaign> campaigns;
}
