package ru.mentola.hunterapi.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mentola.hunterapi.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String token);

    Optional<User> findByName(String name);

    @Transactional
    void removeByToken(String token);
}