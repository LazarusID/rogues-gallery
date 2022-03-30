package dev.dowling.roguesgallery.action;

import dev.dowling.roguesgallery.contract.Guard;
import dev.dowling.roguesgallery.domain.AppUser;
import dev.dowling.roguesgallery.entity.UserEntity;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class GetAppUser implements Callable<AppUser> {

    private Integer userId;
    private Function<Integer, Optional<UserEntity>> getter;

    public GetAppUser(Integer userId, Function<Integer, Optional<UserEntity>> getter) {

        Guard.AgainstNull(userId);
        Guard.AgainstNegative(userId);
        Guard.AgainstZero(userId);

        Guard.AgainstNull(getter);

        this.userId = userId;
        this.getter = getter;
    }

    @Override
    public AppUser call() throws Exception {
        var entityOptional = getter.apply(userId);
        UserEntity entity = entityOptional.get();
        return AppUser.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .name(entity.getName())
                .build();
    }
}
