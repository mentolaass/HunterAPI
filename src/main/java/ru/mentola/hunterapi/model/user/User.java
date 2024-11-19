package ru.mentola.hunterapi.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import ru.mentola.hunterapi.model.inspection.Inspection;

import java.util.List;

@Data
@Entity
@Getter
@Setter
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @JsonProperty("token")
    @Column(name = "token")
    private String token;

    @JsonProperty("role")
    @Column(name = "role")
    private UserRole userRole;

    @JsonProperty("create_timestamp")
    @Column(name = "create_timestamp")
    private long createTimestamp;

    @JsonProperty("inspections")
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Inspection> inspections;
}
