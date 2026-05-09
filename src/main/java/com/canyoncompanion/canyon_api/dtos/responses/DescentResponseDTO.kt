package com.canyoncompanion.canyon_api.dtos.responses

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import java.time.LocalDateTime

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
class DescentResponseDTO {
    private val id: Long? = null

    private val name: String? = null

    private val location: String? = null

    private val province: String? = null

    private val verticalCharacter: String? = null

    private val aquaticCharacter: String? = null

    private val commitment: String? = null

    private val descriptionLink: String? = null

    private val comments: String? = null

    // =========================
    // RELACIÓN SIMPLIFICADA
    // =========================
    private val userId: Long? = null

    private val username: String? = null

    // =========================
    // AUDITORÍA
    // =========================
    private val createdAt: LocalDateTime? = null

    private val updatedAt: LocalDateTime? = null
}