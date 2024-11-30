package ru.mentola.hunterapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mentola.hunterapi.model.inspection.Inspection;
import ru.mentola.hunterapi.model.inspection.InspectionStatus;
import ru.mentola.hunterapi.service.InspectionService;
import ru.mentola.hunterapi.service.InspectionStorageService;
import ru.mentola.hunterapi.service.UserService;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/inspection")
public final class InspectionController {
    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private InspectionStorageService inspectionStorageService;

    @Value("${app.inspection.autoclose}")
    private boolean inspectionAutoclose;

    @PostMapping("/")
    public ResponseEntity<Set<Inspection>> findAllInspections() {
        return ResponseEntity.ok(inspectionService.findAll());
    }

    @PostMapping("/get")
    public ResponseEntity<Inspection> inspection(
            @RequestParam("token") String token
    ) {
        try {
            var qInspection = inspectionService.findByToken(token);
            if (qInspection.isEmpty()) return ResponseEntity.notFound().build();
            var inspection = qInspection.get();
            return ResponseEntity.ok(inspection);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/log")
    public ResponseEntity<Resource> logInspection(
            @RequestParam("token") String token
    ) {
        try {
            return ResponseEntity.ok(inspectionStorageService.getLog(token));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
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
            @RequestParam("file") MultipartFile file
            ) {
        try {
            var qInspection = inspectionService.findByToken(token);
            if (qInspection.isEmpty()) return ResponseEntity.notFound().build();
            var inspection = qInspection.get();
            if (inspection.getCurrentlyStatus() == InspectionStatus.CLOSED) return ResponseEntity.badRequest().build();
            if (System.currentTimeMillis() >= inspection.getUntilTimestamp()
                || inspectionAutoclose) {
                inspection.setCurrentlyStatus(InspectionStatus.CLOSED);
                inspectionService.saveInspection(inspection);
                if (!inspectionAutoclose)
                    return ResponseEntity.badRequest().build();
            }
            inspectionStorageService.updateLog(token, file);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}