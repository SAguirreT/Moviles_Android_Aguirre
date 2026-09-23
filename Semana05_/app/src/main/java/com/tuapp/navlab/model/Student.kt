package com.tuapp.navlab.model

import com.tuapp.navlab.ui.theme.SHEILA_AVATAR_URL

// Modelo de datos que representa a un estudiante del portal académico
data class Student(
    val id: Int,
    val name: String,
    val career: String,
    val avatarUrl: String,
    val studentCode: String,
    val email: String,
    val faculty: String,
    val bio: String
)

// Repositorio estático con la lista de alumnos registrados en el sistema
object StudentRepository {
    val students: List<Student> = listOf(
        Student(
            id = 1,
            name = "Sheila Aguirre",
            career = "Diseño y Desarrollo de Software",
            avatarUrl = SHEILA_AVATAR_URL,
            studentCode = "2024-0512",
            email = "sheila.aguirre@tecsup.edu.pe",
            faculty = "Tecnología Digital",
            bio = "Estudiante destacada enfocada en arquitectura móvil moderna con Jetpack Compose y Kotlin."
        ),
        Student(
            id = 2,
            name = "María García",
            career = "Arquitectura",
            avatarUrl = "https://randomuser.me/api/portraits/women/68.jpg",
            studentCode = "2024-0821",
            email = "maria.garcia@tecsup.edu.pe",
            faculty = "Ingeniería y Construcción",
            bio = "Apasionada por el diseño sostenible, modelado 3D y estructuras urbanas innovadoras."
        ),
        Student(
            id = 3,
            name = "Carlos Perez",
            career = "Medicina",
            avatarUrl = "https://randomuser.me/api/portraits/men/32.jpg",
            studentCode = "2024-0394",
            email = "carlos.perez@tecsup.edu.pe",
            faculty = "Ciencias de la Salud",
            bio = "Investigador en biotecnología médica y tecnologías de diagnóstico clínico avanzado."
        ),
        Student(
            id = 4,
            name = "Ana Lopez",
            career = "Derecho",
            avatarUrl = "https://randomuser.me/api/portraits/women/12.jpg",
            studentCode = "2024-0105",
            email = "ana.lopez@tecsup.edu.pe",
            faculty = "Ciencias Sociales y Legales",
            bio = "Especialista en derecho digital, propiedad intelectual y normativas de ciberseguridad."
        ),
        Student(
            id = 5,
            name = "Luis Ramirez",
            career = "Administración",
            avatarUrl = "https://randomuser.me/api/portraits/men/45.jpg",
            studentCode = "2024-0776",
            email = "luis.ramirez@tecsup.edu.pe",
            faculty = "Negocios Globales",
            bio = "Enfocado en transformación digital de empresas y gestión de proyectos ágiles."
        )
    )
}
