package com.canyoncompanion.canyon_api.repository;

import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);

    @Query("""
    select rt from RefreshTokenEntity rt
    join fetch rt.user u
    where u.id = :userId
""")
    Optional<RefreshTokenEntity> findByUserId(Long userId);

    @Query("SELECT r FROM RefreshTokenEntity r JOIN FETCH r.user WHERE r.token = :token")
    Optional<RefreshTokenEntity> findByTokenWithUser(@Param("token") String token);

    void deleteByUser(UserEntity user);
}

