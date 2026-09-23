# Documentación de Prompts - Fase 2 (Mejora con IA)

## Prompt Principal Utilizado para el Rediseño "Portal Académico Premium"

**Solicitud a la IA:**
> "Actúa como un desarrollador Android senior experto en Kotlin, Jetpack Compose y Material3. YA TENGO un proyecto funcional (hasta el commit 5) con navegación entre 4 pantallas (Home, List, Detail, Profile) usando Navigation Compose con una sealed class Screen... El objetivo final es un rediseño visual completo estilo Portal Académico Premium, con una paleta de tonos violeta/lila neón exclusiva y diferenciada..."

**Estructura y Puntos Claves Implementados:**
1. **Modelos y Tema:** Creación de `model/Student.kt` y constante de colores en `ui.theme/Colors.kt`.
2. **Navegación:** Integración de `LoginScreen.kt` en `Screen.kt` y `AppNavigation.kt`.
3. **Pantallas:** Rediseño completo de `HomeScreen`, `ListScreen`, `DetailScreen` y `ProfileScreen` con estilo Premium (cards flotantes, 3. **Pantallas:** Rediseño completo de HomeScreen, ListScreen, DetailScreen y ProfileScreen con estilo Premium (cards flotantes, gradientes neón y avatares).
4. **Cierre de sesión:** Flujo de limpieza de backstack con popUpTo(0).

## REQUISITOS CUMPLIDOS
- Uso de AsyncImage de Coil en avatares.
- Fotos reales de randomuser.me.
- Coincidencia del primer alumno en Directorio con el Perfil.
- Uso exclusivo de las constantes de Colors.kt.
- Navegación limpia mediante popBackStack() y popUpTo().
