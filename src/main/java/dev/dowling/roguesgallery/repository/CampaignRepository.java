package dev.dowling.roguesgallery.repository;

import dev.dowling.roguesgallery.entity.CampaignEntity;
import org.springframework.data.repository.CrudRepository;

public interface CampaignRepository extends CrudRepository<CampaignEntity, Integer> {
}
