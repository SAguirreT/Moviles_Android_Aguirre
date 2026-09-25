# Registro de Prompts y Requerimientos Funcionales - Clínica Salud+

Este documento contiene la especificación de requerimientos funcionales, los prompts ejecutados mediante la asistencia de IA (Gemini en Android Studio) para la modificación del código existente, las correcciones de errores aplicadas y los mensajes de commit asociados.

---

## 📋 1. Requerimientos Funcionales Completos (RF)

| Código | Requerimiento | Descripción |
| :--- | :--- | :--- |
| **RF-01** | **Gestión de Estado Favorito en Datos** | El sistema debe permitir almacenar el estado de "favorito" (`Boolean`) para cada médico en la entidad `Doctor` y disponer de estados iniciales en la fuente de datos local `MockData`. |
| **RF-02** | **Búsqueda en Tiempo Real** | La aplicación debe ofrecer un campo de texto de búsqueda (`OutlinedTextField`) en la pantalla principal que filtre la lista de médicos en tiempo real por su nombre o especialidad. |
| **RF-03** | **Marcado/Desmarcado de Favoritos** | Las tarjetas de médico (`DoctorCard`) deben incluir un botón interactivo (ícono de corazón) que permita alternar dinámicamente entre favorito y no favorito. |
| **RF-04** | **Filtrado Combinado de Información** | La pantalla principal debe permitir combinar múltiples filtros simultáneos: búsqueda por texto, selección de especialidad mediante chips y un filtro exclusivo para mostrar únicamente médicos favoritos. |

---

## 💬 2. Prompts, Modificaciones y Commits

### Prompt 1: Actualización del Modelo y Fuente de Datos (RF-01)

> **Prompt:**
> *"Modifica el archivo `Doctor.kt` agregando el parámetro `val isFavorite: Boolean = false` a la data class. Luego, actualiza `MockData.kt` en el objeto `doctors` pasando el parámetro `isFavorite` (algunos en `true` y otros en `false`). No elimines ni crees nuevos archivos, solo actualiza las clases existentes manteniendo el paquete `com.aguirre.clinicasalud`."*

* **Qué se pidió:** Extender el modelo de datos sin romper la estructura existente.
* **Archivos modificados:**
    * `model/Doctor.kt`: Se añadió la propiedad `val isFavorite: Boolean = false`.
    * `data/MockData.kt`: Se agregaron los valores del parámetro `isFavorite` en la lista de médicos de prueba.
* **Commit:**
  ```bash
  git commit -m "feat(model): agregar campo isFavorite a Doctor y actualizar MockData"