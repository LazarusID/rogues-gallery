package dev.dowling.roguesgallery.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Builder
@Data
public class AppUser {
    private final int id;
    private final String email;
    private final String name;

    private final List<Campaign> campaigns;
}
