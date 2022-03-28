package dev.dowling.roguesgallery.controller;

import dev.dowling.roguesgallery.domain.AppUser;
import dev.dowling.roguesgallery.entity.UserEntity;
import dev.dowling.roguesgallery.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    private UserRepository repository;

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

}
