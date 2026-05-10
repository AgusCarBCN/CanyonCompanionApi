package com.canyoncompanion.canyon_api.service

import com.canyoncompanion.canyon_api.model.entities.RefreshTokenEntity
import org.springframework.security.core.userdetails.UserDetails


interface RefreshTokenService {
    fun findByToken(token: String?): RefreshTokenEntity?

    fun createOrRefreshToken(userDetails: UserDetails?): String?

    fun rotateToken(oldToken: RefreshTokenEntity?): String?
}