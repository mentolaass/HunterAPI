package ru.mentola.hunterapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public final class ServiceStatus {
    @JsonProperty("schema_version")
    private final String schemaVersion;
}