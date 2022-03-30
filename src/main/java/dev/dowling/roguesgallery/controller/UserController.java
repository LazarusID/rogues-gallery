package dev.dowling.roguesgallery.controller;

import dev.dowling.roguesgallery.domain.AppUser;
import dev.dowling.roguesgallery.domain.Campaign;
import dev.dowling.roguesgallery.entity.UserEntity;
import dev.dowling.roguesgallery.repository.UserRepository;
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

    public UserController(UserRepository userRepository) {
        repository = userRepository;
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
    public AppUser getUser(@PathVariable Integer id) {
        var entity = repository.findById(id);

        if (entity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var e = entity.get();
        return AppUser.builder()
                .id(e.getId())
                .build();
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
    public AppUser getCampaigns(@PathVariable Integer id) {
        var userentity = repository.findById(id);

        if (userentity.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        var e = userentity.get();

        List<Campaign> campaigns = e.getCampaigns().stream()
                .map(c -> {
                    return Campaign.builder()
                            .id(c.getId())
                            .name(c.getName())
                            .active(c.getActive())
                            .userId(c.getUserId())
                            .build();
                })
                .collect(Collectors.toList());

        return AppUser.builder()
                .id(e.getId())
                .name(e.getName())
                .email(e.getEmail())
                .campaigns(campaigns)
                .build();
    }


}
