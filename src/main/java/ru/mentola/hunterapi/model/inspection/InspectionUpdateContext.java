package ru.mentola.hunterapi.model.inspection;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public final class InspectionUpdateContext {
    @JsonProperty("update_type")
    private final UpdateContextType updateContextType;
    @JsonProperty("log")
    private final String log;
}