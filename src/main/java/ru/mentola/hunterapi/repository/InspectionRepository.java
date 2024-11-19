package ru.mentola.hunterapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mentola.hunterapi.model.inspection.Inspection;

import java.util.Optional;
import java.util.Set;

public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    Set<Inspection> findAllByOwner(long id);

    Optional<Inspection> findByToken(String token);
}