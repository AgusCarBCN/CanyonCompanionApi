package com.canyoncompanion.canyon_api.repository;

import com.canyoncompanion.canyon_api.model.entities.RefreshToken;
import com.canyoncompanion.canyon_api.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    @Query("""
    select rt from RefreshToken rt
    join fetch rt.user u
    where u.id = :userId
""")
    Optional<RefreshToken> findByUserId(Long userId);

    Optional<RefreshToken> findByUser(UserEntity user);

    @Query("SELECT r FROM RefreshToken r JOIN FETCH r.user WHERE r.token = :token")
    Optional<RefreshToken> findByTokenWithUser(@Param("token") String token);

    void deleteByUser(UserEntity user);

    void deleteByToken(String token);
}

