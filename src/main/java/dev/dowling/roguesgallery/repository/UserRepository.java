package dev.dowling.roguesgallery.repository;

import dev.dowling.roguesgallery.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
}
