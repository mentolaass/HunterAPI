package ru.mentola.hunterapi.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IInspectionStorageService {
    void init();

    void updateLog(String token, MultipartFile log);

    Resource getLog(String token);

    Set<String> getLogs();
}
