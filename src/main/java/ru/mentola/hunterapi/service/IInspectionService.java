package ru.mentola.hunterapi.service;

import ru.mentola.hunterapi.model.inspection.Inspection;
import ru.mentola.hunterapi.model.user.User;

import java.util.Optional;
import java.util.Set;

public interface IInspectionService {
    Inspection saveInspection(Inspection inspection);

    Inspection generateInspection(User owner);

    Optional<Inspection> findByToken(String token);

    Set<Inspection> findAllByUser(User user);

    Set<Inspection> findAll();

    boolean inspectionIsActive(Inspection inspection);
}