package ru.mentola.hunterapi.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface IInspectionStorage {
    void init();

    void save(String name, MultipartFile file);

    Resource load(String name);

    Set<String> getNames();
}