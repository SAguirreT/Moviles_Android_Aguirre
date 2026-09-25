package com.aguirre.clinicasalud.data
import com.aguirre.clinicasalud.model.Appointment
import com.aguirre.clinicasalud.model.Doctor
// 📌 SUSTENTACIÓN: Singleton para simular la base de datos local de pruebas
object MockData {
    val specialties = listOf("Cardiología", "Pediatría", "Dermatología", "Neurología")

    val doctors = listOf(
        Doctor(
            id = 1,
            name = "Dra. Ana Torres",
            specialty = "Cardióloga",
            rating = 4.9,
            reviewCount = 128,
            experience = "12 años exp.",
            bio = "Especialista en arritmias e hipertensión, formación en la Clínica Mayo.",
            isFavorite = true
        ),
        Doctor(
            id = 2,
            name = "Dr. Luis Vega",
            specialty = "Pediatra",
            rating = 4.7,
            reviewCount = 95,
            experience = "8 años exp.",
            bio = "Atención integral infantil y desarrollo pediátrico.",
            isFavorite = false
        ),
        Doctor(
            id = 3,
            name = "Dra. Rosa Díaz",
            specialty = "Dermatóloga",
            rating = 4.8,
            reviewCount = 110,
            experience = "10 años exp.",
            bio = "Especialista en dermatología clínica y estética.",
            isFavorite = true
        )
    )

    val initialAppointments = listOf(
        Appointment(1, "Dra. Ana Torres", "Viernes 27", "10:30 am", "Confirmada"),
        Appointment(2, "Dr. Luis Vega", "Miércoles 15", "3:00 pm", "Completada")
    )

    val availableDates = listOf("Jue 26", "Vie 27", "Sáb 28")
    val availableTimes = listOf("9:00", "10:30", "3:00")
}