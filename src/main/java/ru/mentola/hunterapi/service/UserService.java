package ru.mentola.hunterapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mentola.hunterapi.model.user.User;
import ru.mentola.hunterapi.model.user.UserRole;
import ru.mentola.hunterapi.repository.UserRepository;

import java.util.*;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findByToken(String token) {
        return userRepository.findByToken(token);
    }

    @Override
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public void removeByToken(String token) {
        userRepository.removeByToken(token);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Set<User> findAllBy(Predicate<User> filter) {
        return getAll()
                .stream()
                .filter(filter)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getAll() {
        return new HashSet<>(userRepository.findAll());
    }

    @Override
    public User generateUser(String name) {
        return User.builder()
                .name(name)
                .token(UUID.randomUUID().toString())
                .inspections(List.of())
                .createTimestamp(System.currentTimeMillis())
                .userRole(UserRole.DEFAULT)
                .build();
    }
}
