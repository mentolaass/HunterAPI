package ru.mentola.hunterapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mentola.hunterapi.storage.InspectionStorage;

import java.util.Set;

@Service
public class InspectionStorageService implements IInspectionStorageService {
    @Autowired
    private InspectionStorage inspectionStorage;

    @Override
    public void init() {
        inspectionStorage.init();
    }

    @Override
    public void updateLog(String token, MultipartFile log) {
        inspectionStorage.save(token, log);
    }

    @Override
    public Resource getLog(String token) {
        return inspectionStorage.load(token);
    }

    @Override
    public Set<String> getLogs() {
        return inspectionStorage.getNames();
    }
}
