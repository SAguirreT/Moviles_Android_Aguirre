package com.example.clinicasalud.model

// 📌 SUSTENTACIÓN: Modelo para las citas guardadas
data class Appointment(
    val id: Int,
    val doctorName: String,
    val date: String,
    val time: String,
    val status: String // "Confirmada" o "Completada"
)