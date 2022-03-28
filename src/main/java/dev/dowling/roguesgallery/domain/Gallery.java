package dev.dowling.roguesgallery.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Gallery {
    private final int id;
    private final int campaignId;
    private final String name;
    private final String content;
    private final String cache;
    private final boolean active;
}
