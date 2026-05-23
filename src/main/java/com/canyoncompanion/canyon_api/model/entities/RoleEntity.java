package com.canyoncompanion.canyon_api.model.entities;

import com.canyoncompanion.canyon_api.model.enums.Roles;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "roles")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Roles role;
}
