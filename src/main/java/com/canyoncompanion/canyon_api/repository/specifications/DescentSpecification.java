package com.canyoncompanion.canyon_api.repository.specifications;

import com.canyoncompanion.canyon_api.model.entities.DescentEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import com.canyoncompanion.canyon_api.model.enums.AquaticCharacter;
import com.canyoncompanion.canyon_api.model.enums.Commitment;
import com.canyoncompanion.canyon_api.model.enums.VerticalCharacter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class DescentSpecification {

    public static Specification<DescentEntity> filter(
            UserEntity user,
            String name,
            String location,
            String province,
            VerticalCharacter verticalCharacter,
            AquaticCharacter aquaticCharacter,
            Commitment commitment
    ) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // =========================
            // USER
            // =========================
            if (user != null) {
                predicates.add(
                        cb.equal(root.get("user"), user)
                );
            }

            // =========================
            // NAME
            // =========================
            if (name != null && !name.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            // =========================
            // LOCATION
            // =========================
            if (location != null && !location.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("location")),
                                "%" + location.toLowerCase() + "%"
                        )
                );
            }

            // =========================
            // PROVINCE
            // =========================
            if (province != null && !province.isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("province")),
                                "%" + province.toLowerCase() + "%"
                        )
                );
            }

            // =========================
            // VERTICAL CHARACTER
            // =========================
            if (verticalCharacter != null) {
                predicates.add(
                        cb.equal(root.get("verticalCharacter"), verticalCharacter)
                );
            }
            // =========================
            // AQUATIC CHARACTER
            // =========================
            if (aquaticCharacter != null) {
                predicates.add(
                        cb.equal(root.get("aquaticCharacter"), aquaticCharacter)
                );
            }
            // =========================
            // COMMITMENT
            // =========================
            if (commitment != null) {
                predicates.add(
                        cb.equal(root.get("commitment"), commitment)
                );
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
