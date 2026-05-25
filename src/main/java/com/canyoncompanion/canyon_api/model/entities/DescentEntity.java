package com.canyoncompanion.canyon_api.model.entities;


import com.canyoncompanion.canyon_api.model.enums.AquaticCharacter;
import com.canyoncompanion.canyon_api.model.enums.Commitment;
import com.canyoncompanion.canyon_api.model.enums.VerticalCharacter;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Slf4j
@Table(name = "descents")
public class DescentEntity {

   //@GeneratedValue(strategy = GenerationType.IDENTITY)
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    // =========================
    // RELATIONSHIP
    // =========================
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(
            mappedBy = "descent",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<DescentImageEntity> images = new ArrayList<>();
    // =========================
    // BASIC INFO
    // =========================
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String province;

    // =========================
    // CHARACTERISTICS
    // =========================
    @Column(name = "vertical_character")
    @Enumerated(EnumType.STRING)
    private VerticalCharacter verticalCharacter;

    @Column(name = "aquatic_character")
    @Enumerated(EnumType.STRING)
    private AquaticCharacter aquaticCharacter;

    @Column(name = "commitment")
    @Enumerated(EnumType.STRING)
    private Commitment commitment;

    // =========================
    // LINKS / DESCRIPTION
    // =========================
    @Column(name = "description_link", length = 255)
    private String descriptionLink;

    @Column(columnDefinition = "TEXT")
    private String comments;

    // =========================
    // AUDIT
    // =========================
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public void addImage(DescentImageEntity image) {
        images.add(image);
        image.setDescent(this);
    }
    public void removeImage(DescentImageEntity image) {
        images.remove(image);
        image.setDescent(null);
    }

}