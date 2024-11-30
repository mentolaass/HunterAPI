package ru.mentola.hunterapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.mentola.hunterapi.model.inspection.Inspection;
import ru.mentola.hunterapi.model.inspection.InspectionStatus;
import ru.mentola.hunterapi.model.user.User;
import ru.mentola.hunterapi.repository.InspectionRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public final class InspectionService implements IInspectionService {
    @Autowired
    private InspectionRepository inspectionRepository;

    @Value("${app.version}")
    private String version;

    @Value("${app.inspection.alive}")
    private int inspectionAlive;

    @Override
    public Inspection saveInspection(Inspection inspection) {
        return inspectionRepository.save(inspection);
    }

    @Override
    public Inspection generateInspection(User owner) {
        return Inspection.builder()
                .owner(owner.getId())
                .currentlyStatus(InspectionStatus.OPENED)
                .createTimestamp(System.currentTimeMillis())
                .untilTimestamp(System.currentTimeMillis() + (inspectionAlive * 60000L))
                .lastUpdateTimestamp(System.currentTimeMillis())
                .token(UUID.randomUUID().toString())
                .schemaVersion(version)
                .build();
    }

    @Override
    public Optional<Inspection> findByToken(String token) {
        return inspectionRepository.findByToken(token);
    }

    @Override
    public Set<Inspection> findAllByUser(User user) {
        return inspectionRepository.findAllByOwner(user.getId());
    }

    @Override
    public Set<Inspection> findAll() {
        return new HashSet<>(inspectionRepository.findAll());
    }

    @Override
    public boolean inspectionIsActive(Inspection inspection) {
        if (inspection.getCurrentlyStatus() == InspectionStatus.CLOSED)
            return false;
        if (System.currentTimeMillis() >= inspection.getUntilTimestamp()) {
            inspection.setCurrentlyStatus(InspectionStatus.CLOSED);
            this.saveInspection(inspection);
            return false;
        }
        return true;
    }
}