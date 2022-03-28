package dev.dowling.roguesgallery.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Campaign {
    private final int id;
    private final int userId;
    private final String name;
    private final boolean active;

    private final List<Gallery> galleries;
}
