package com.example.clinicasalud.navigation

// 📌 SUSTENTACIÓN: Clase sellada para evitar errores al tipear nombres de rutas
sealed class Screen(val route: String) {
    object Home : Screen("home")

    // 📌 SUSTENTACIÓN: Definición de rutas paramétricas
    object DoctorProfile : Screen("doctor_profile/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_profile/$doctorId"
    }

    object ScheduleAppointment : Screen("schedule_appointment/{doctorId}") {
        fun createRoute(doctorId: Int) = "schedule_appointment/$doctorId"
    }

    object Confirmation : Screen("confirmation/{doctorName}/{date}/{time}") {
        fun createRoute(doctorName: String, date: String, time: String) =
            "confirmation/$doctorName/$date/$time"
    }

    object MyAppointments : Screen("my_appointments")
    object MedicalHistory : Screen("medical_history")
    object Profile : Screen("profile")
}