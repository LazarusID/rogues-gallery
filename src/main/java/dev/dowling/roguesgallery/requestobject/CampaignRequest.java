package dev.dowling.roguesgallery.requestobject;

import lombok.Data;

@Data
public class CampaignRequest {
    private String name;
    private boolean active;
}
