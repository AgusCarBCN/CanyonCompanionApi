package com.canyoncompanion.canyon_api.service

import com.canyoncompanion.canyon_api.exception.BusinessException
import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity
import com.canyoncompanion.canyon_api.model.entities.UserEntity
import com.canyoncompanion.canyon_api.repository.RefreshTokenRepository
import com.canyoncompanion.canyon_api.repository.UserRepository
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID
import java.util.function.Supplier


@Service
@RequiredArgsConstructor
class RefreshTokenServiceImpl : RefreshTokenService {
    private val repository: RefreshTokenRepository? = null
    private val userRepository: UserRepository? = null

    // =====================================================
    // FIND TOKEN
    // =====================================================
    override fun findByToken(token: String?): RefreshTokenEntity? {
        return repository!!.findByToken(token)
            .orElseThrow<BusinessException?>(Supplier {
                BusinessException(
                    "Invalid refresh token",
                    "REFRESH_TOKEN_INVALID",
                    HttpStatus.UNAUTHORIZED
                )
            }
            )
    }

    override fun createOrRefreshToken(userDetails: UserDetails?): String? {
        val user = userRepository!!.findByEmail(userDetails?.username)
            .orElseThrow()

        return generateAndSaveToken(user)
    }

    override fun rotateToken(oldToken: RefreshTokenEntity?): String? {
        // 1. invalidar token anterior

        repository!!.delete(oldToken)

        // 2. generar nuevo token
        return generateAndSaveToken(oldToken?.user)
    }




    // =====================================================
    // CORE GENERATION
    // =====================================================
    private fun generateAndSaveToken(user: UserEntity?): String? {
        val token = RefreshTokenEntity.builder()
            .token(UUID.randomUUID().toString())
            .user(user)
            .expiryDate(LocalDateTime.now().plusDays(7) as Instant?)
            .build()

        repository!!.save<RefreshTokenEntity?>(token)

        return token.getToken()
    }
}