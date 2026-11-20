# Registro de Interacciones con Gemini — Mejora IA (Laboratorio 14 CentroTareas)

Este archivo documenta las consultas (prompts) realizadas a Gemini, sus respuestas y las decisiones arquitectónicas adoptadas para la rama `mejora-ia`.

## Mejora 1: Progreso reactivo en el Repository mediante Flow
- **Prompt a Gemini:** *"¿Cómo puedo mover la emisión de progreso de una tarea pesada desde el ViewModel hacia el Repository utilizando Kotlin Flow en una arquitectura MVVM?"*
- **Respuesta de Gemini:** Utilizar el builder `flow { ... }` en el Repository para emitir valores progresivos con `emit()` y retrasos `delay()`, y consumir dicho `Flow` en el ViewModel mediante `collect`.
- **Decisión adoptada:** Se implementó `observarProgreso(pasos: Int): Flow<Float>` en `TareasRepository` y `TareasRepositoryLocal`, desacoplando la lógica de temporización y progreso de la capa de presentación.

## Mejora 2: Reintentos automáticos con espera progresiva (Backoff Exponencial)
- **Prompt a Gemini:** *"¿Cuál es la mejor manera de implementar reintentos automáticos con espera progresiva (backoff exponencial) en corrutinas cuando una petición asíncrona falla por error de red?"*
- **Respuesta de Gemini:** Crear un bucle de intentos (`while` o función de extensión) que capture excepciones, incremente el contador de intentos y aplique un `delay` proporcional al intento actual (`delay(intentos * 1000L)`).
- **Decisión adoptada:** Se agregó `cargarConReintentos(simularError)` en `TareasViewModel`, reintentando hasta 3 veces con demoras de 1s y 2s.

## Mejora 3: Historial comparativo de tiempos de carga
- **Prompt a Gemini:** *"¿Cómo puedo registrar y mostrar un historial de tiempos de ejecución comparativos entre carga secuencial y paralela en Jetpack Compose con StateFlow?"*
- **Respuesta de Gemini:** Crear un data class de registro (`CargaRecord`), mantener una lista inmutable en un `MutableStateFlow` dentro del ViewModel, y renderizarla mediante un bucle en una `Column` de Compose.
- **Decisión adoptada:** Se creó `CargaRecord`, el StateFlow `historialCargas` y la sección visual "Historial Comparativo" en `PantallaTareas.kt`.
