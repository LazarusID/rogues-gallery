package dev.dowling.roguesgallery.action;

import dev.dowling.roguesgallery.contract.Guard;
import dev.dowling.roguesgallery.domain.AppUser;
import dev.dowling.roguesgallery.domain.Campaign;
import dev.dowling.roguesgallery.entity.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetAppUser implements Callable<AppUser> {

    private Integer userId;
    private Function<Integer, Optional<UserEntity>> getter;
    private boolean loadCampaigns;

    public GetAppUser(Integer userId, Function<Integer, Optional<UserEntity>> getter) {

        Guard.AgainstNull(userId);
        Guard.AgainstNegative(userId);
        Guard.AgainstZero(userId);

        Guard.AgainstNull(getter);

        this.userId = userId;
        this.getter = getter;
        this.loadCampaigns = false;
    }

    public GetAppUser withCampaigns() {
        loadCampaigns = true;
        return this;
    }

    @Override
    public AppUser call() throws Exception {
        var entityOptional = getter.apply(userId);

        if (entityOptional.isEmpty()) {
            return AppUser.emptyUser;
        }

        var entity = entityOptional.get();
        List<Campaign> campaignList = null;
        if (loadCampaigns) {
            campaignList = populateCampaigns(entity);
        }

        return AppUser.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .name(entity.getName())
            .campaigns(campaignList)
            .build();
    }

    private List<Campaign> populateCampaigns(UserEntity entity) {
        if (entity.getCampaigns() != null) {
            return
                    entity.getCampaigns().stream()
                            .map(ce -> {
                                return Campaign.builder()
                                        .id(ce.getId())
                                        .userId(ce.getUserId())
                                        .name(ce.getName())
                                        .active(ce.getActive())
                                        .build();
                            })
                            .collect(Collectors.toList());
        }
        return null;
    }

}
