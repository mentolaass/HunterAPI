package ru.mentola.hunterapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mentola.hunterapi.model.inspection.Inspection;
import ru.mentola.hunterapi.model.inspection.InspectionStatus;
import ru.mentola.hunterapi.model.inspection.InspectionUpdateContext;
import ru.mentola.hunterapi.model.inspection.UpdateContextType;
import ru.mentola.hunterapi.service.InspectionService;
import ru.mentola.hunterapi.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/inspection")
public final class InspectionController {
    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public ResponseEntity<Set<Inspection>> findAllInspections() {
        return ResponseEntity.ok(inspectionService.findAll());
    }

    @PostMapping("/create")
    public ResponseEntity<Inspection> createInspection(
            @RequestHeader("Authorization") String authorization
    ) {
        String jwt = authorization.substring(7);
        var user = userService.findByToken(jwt);
        if (user.isEmpty()) return ResponseEntity.status(403).build();
        var inspection = inspectionService.generateInspection(user.get());
        inspection = inspectionService.saveInspection(inspection);
        return ResponseEntity.ok(inspection);
    }

    @PostMapping("/close")
    public ResponseEntity<?> closeInspection(
            @RequestParam("token") String token
    ) {
        var qInspection = inspectionService.findByToken(token);
        if (qInspection.isEmpty()) return ResponseEntity.notFound().build();
        var inspection = qInspection.get();
        if (inspection.getCurrentlyStatus() == InspectionStatus.CLOSED) return ResponseEntity.badRequest().build();
        inspection.setCurrentlyStatus(InspectionStatus.CLOSED);
        inspectionService.saveInspection(inspection);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateInspection(
            @RequestParam("token") String token,
            @RequestBody InspectionUpdateContext updateContext
            ) {
        var qInspection = inspectionService.findByToken(token);
        if (qInspection.isEmpty()) return ResponseEntity.notFound().build();
        var inspection = qInspection.get();
        if (!inspectionService.inspectionIsActive(inspection)) return ResponseEntity.badRequest().build();
        if (updateContext.getUpdateContextType() == UpdateContextType.OVERWRITE) {
            inspection.setLog(updateContext.getLog());
        } else if (updateContext.getUpdateContextType() == UpdateContextType.ADD) {
            inspection.setLog(inspection.getLog() + "\n\n" + updateContext.getLog());
        }
        inspection.setLastUpdateTimestamp(System.currentTimeMillis());
        inspectionService.saveInspection(inspection);
        return ResponseEntity.ok().build();
    }
}