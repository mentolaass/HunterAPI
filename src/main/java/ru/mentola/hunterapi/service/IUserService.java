package ru.mentola.hunterapi.service;

import ru.mentola.hunterapi.model.user.User;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface IUserService {
    Optional<User> findByToken(String token);

    Optional<User> findByName(String name);

    void removeByToken(String token);

    User saveUser(User user);

    Set<User> findAllBy(Predicate<User> filter);

    Set<User> getAll();

    User generateUser(String name);
}