package ru.mentola.hunterapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mentola.hunterapi.model.user.User;
import ru.mentola.hunterapi.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/user")
public final class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Set<User>> findAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/{name}")
    public ResponseEntity<User> findUser(
            @PathVariable String name
    ) {
        var user = userService.findByName(name);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("create")
    public ResponseEntity<User> createUser(
            @RequestParam(name = "name") String name
    ) {
        var user = userService.generateUser(name);
        user = userService.saveUser(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("remove")
    public ResponseEntity<?> removeUser(
            @RequestHeader("Authorization") String authorization,
            @RequestParam(name = "token") String token
    ) {
        if (userService.findByToken(token).isEmpty()) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
        String jwt = authorization.substring(7);
        if (token.equals(jwt)) return ResponseEntity.badRequest().build();
        userService.removeByToken(token);
        return ResponseEntity.ok().build();
    }
}
