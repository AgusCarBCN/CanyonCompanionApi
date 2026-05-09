package com.canyoncompanion.canyon_api.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_seq")
    @SequenceGenerator(
            name = "refresh_seq",
            sequenceName = "refresh_seq",
            allocationSize = 1
    )

    private Long id;
    @Column(nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private Instant expiryDate;

}
