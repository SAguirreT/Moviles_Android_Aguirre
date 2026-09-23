package com.aguirre.clinicaapp.model

data class Doctor(
    val idMedico: Int,
    val nombreCompleto: String,
    val especialidadMedica: String,
    val colegiaturaCmp: String,
    val correoContacto: String,
    val ubicacionConsultorio: String,
    val resumenProfesional: String,
    val fotoPerfilUrl: String
)