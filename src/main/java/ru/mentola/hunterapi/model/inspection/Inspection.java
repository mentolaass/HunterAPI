package ru.mentola.hunterapi.model.inspection;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Builder
@Table(name = "inspections")
@NoArgsConstructor
@AllArgsConstructor
public final class Inspection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("schema_version")
    @Column(name = "schema_version")
    private String schemaVersion;

    @JsonProperty("create_timestamp")
    @Column(name = "create_timestamp")
    private long createTimestamp;

    @JsonProperty("last_update_timestamp")
    @Column(name = "last_update_timestamp")
    private long lastUpdateTimestamp;

    @JsonProperty("until_timestamp")
    @Column(name = "until_timestamp")
    private long untilTimestamp;

    @JsonProperty("currently_status")
    @Column(name = "currently_status")
    private InspectionStatus currentlyStatus;

    @JsonProperty("owner")
    @Column(name = "owner")
    private long owner;

    @JsonProperty("access_token")
    @Column(name = "access_token")
    private String token;
}