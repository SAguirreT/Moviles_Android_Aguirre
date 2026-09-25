package com.aguirre.clinicasalud.model

// 📌 SUSTENTACIÓN: Entidad de médicos inmutable
data class Doctor(
    val id: Int,
    val name: String,
    val specialty: String,
    val rating: Double,
    val reviewCount: Int,
    val experience: String,
    val bio: String
)