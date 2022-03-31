package dev.dowling.roguesgallery.controller;

import dev.dowling.roguesgallery.action.GetAppUser;
import dev.dowling.roguesgallery.domain.AppUser;
import dev.dowling.roguesgallery.domain.Campaign;
import dev.dowling.roguesgallery.entity.CampaignEntity;
import dev.dowling.roguesgallery.entity.UserEntity;
import dev.dowling.roguesgallery.repository.CampaignRepository;
import dev.dowling.roguesgallery.repository.UserRepository;
import dev.dowling.roguesgallery.requestobject.CampaignRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserRepository repository;
    private final CampaignRepository campaignRepository;

    public UserController(UserRepository userRepository, CampaignRepository campaignRepository)
    {
        repository = userRepository;
        this.campaignRepository = campaignRepository;
    }

    @GetMapping(value = "/", produces = "application/json")
    public List<AppUser> getUsers() {
        List<AppUser> result = new ArrayList<>();
        var userEntities = repository.findAll();

        userEntities.forEach(ue -> {
            result.add(AppUser.builder()
                    .id(ue.getId())
                    .name(ue.getName())
                    .email(ue.getEmail())
                    .build());
        });

        return result;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public AppUser getUser(@PathVariable Integer id) throws Exception {
        var user = new GetAppUser(id, repository::findById).call();
        if (user == AppUser.emptyUser) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @PostMapping("/")
    public UserEntity saveUser(@RequestBody AppUser user) {
        UserEntity newuser = new UserEntity(user.getId(), user.getEmail(), user.getName());
        return repository.save(newuser);
    }

    @PutMapping(value = "/{id}", produces = "application/json")
    public UserEntity updateUser(@PathVariable Integer id, @RequestBody AppUser user) {
        var target = repository.findById(id);

        if (target.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        var entity = target.get();
        entity.setEmail(user.getEmail());
        entity.setName(user.getName());

        return repository.save(entity);
    }

    @DeleteMapping(value = "/{id}", produces = "text/plain")
    public String deleteUser(@PathVariable Integer id) {
        var entity = repository.findById(id);

        if (entity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.delete(entity.get());

        return "Deleted";
    }

    @GetMapping(value = "/{id}/campaign/", produces = "application/json")
    public AppUser getCampaigns(@PathVariable Integer id) throws Exception {
        var user = new GetAppUser(id, repository::findById).withCampaigns().call();
        if (AppUser.emptyUser == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @PostMapping(value = "/{id}/campaign/", produces = "application/json")
    public CampaignEntity createCampaign(@PathVariable Integer id, @RequestBody CampaignRequest campaign) throws Exception {
        var user = new GetAppUser(id, repository::findById).call();

        if (AppUser.emptyUser == user) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        CampaignEntity ce = new CampaignEntity(id, campaign.getName(), campaign.isActive());
        campaignRepository.save(ce);

        return ce;
    }

}
