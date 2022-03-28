package dev.dowling.roguesgallery.repository;

import dev.dowling.roguesgallery.entity.GalleryEntity;
import org.springframework.data.repository.CrudRepository;

public interface GalleryRepository extends CrudRepository<GalleryEntity, Integer> {
}
