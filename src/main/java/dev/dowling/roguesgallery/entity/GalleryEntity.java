package dev.dowling.roguesgallery.entity;

import javax.persistence.*;

@Table(name = "gallery")
@Entity
public class GalleryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="campaign_id")
    private Integer campaignId;
    private String name;
    private String content;
    private String cache;
    private Boolean active;

    protected GalleryEntity() {};

    public GalleryEntity(Integer campaignId, String name, String content, boolean active) {
        this.campaignId = campaignId;
        this.name = name;
        this.content = content;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
