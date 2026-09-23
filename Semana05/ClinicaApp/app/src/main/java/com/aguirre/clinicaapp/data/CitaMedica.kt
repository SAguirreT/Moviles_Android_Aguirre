package com.aguirre.clinicaapp.data

import com.aguirre.clinicaapp.model.Doctor

object ProviderClinico {
    val listaMedicos = listOf(
        Doctor(
            idMedico = 1,
            nombreCompleto = "Dra. Sheila Aguirre",
            especialidadMedica = "Cardiología",
            colegiaturaCmp = "CMP 45892",
            correoContacto = "s.aguirre@clinica.pe",
            ubicacionConsultorio = "Consultorio 302 - Torre A",
            resumenProfesional = "Especialista en cardiología clínica con amplia experiencia.",
            fotoPerfilUrl = "https://randomuser.me/api/portraits/women/44.jpg"
        ),
        Doctor(
            idMedico = 2,
            nombreCompleto = "Dr. Carlos Mendoza",
            especialidadMedica = "Pediatría",
            colegiaturaCmp = "CMP 38210",
            correoContacto = "c.mendoza@clinica.pe",
            ubicacionConsultorio = "Consultorio 105 - Torre B",
            resumenProfesional = "Atención integral infantil y desarrollo pediátrico.",
            fotoPerfilUrl = "https://randomuser.me/api/portraits/men/32.jpg"
        )
    )
}