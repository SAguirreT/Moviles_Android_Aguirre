# Registro de Prompts e Interacciones con Inteligencia Artificial (Gemini)

- **Autora:** Sheila Aguirre
- **Laboratorio:** 09 – Servicio Web
- **Proyecto:** MuroCampus
- **Rama:** `mejora-ia`

## Descripción de las mejoras implementadas
1. **Buscador de publicaciones por título:** Filtrado en tiempo real sobre la lista descargada con soporte para limpiar búsqueda y combinar con el filtro por usuario.
2. **Eliminación de publicaciones mediante DELETE:** Integración del endpoint DELETE con Retrofit `Response<Unit>`, diálogo de confirmación y manejo de simulación de JSONPlaceholder.
3. **Edición de publicaciones mediante PATCH:** Pantalla independiente para modificar el título mediante PATCH, mostrando el título original, validando campos no vacíos e informando la simulación del servidor.
4. **Rediseño profesional de la interfaz:** Aplicación de identidad visual corporativa/académica en tonos azules (`#1565C0`, `#0288D1`, `#0D47A1`), tarjetas compactas, bordes redondeados y soporte completo para modo oscuro con Material 3.

---

## Tabla de Prompts, Respuestas y Decisiones Técnicas

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| "Cómo implementar un buscador en tiempo real sobre una `LazyColumn` en Jetpack Compose manteniendo el filtrado por usuario activo sin llamadas repetitivas innecesarias a la API?" | Gemini sugirió utilizar `MutableStateFlow` combinando `allPosts`, `usuarioFiltro` y `busquedaQuery` dentro de una función de filtrado unificada en el ViewModel. | Ajusté la lógica para que ignore mayúsculas/minúsculas de manera robusta (`lowercase()`) y maneje correctamente estados vacíos. | Para asegurar rendimiento óptimo en memoria y evitar consultas innecesarias a la API por cada caracter ingresado en el buscador. |
| "Cómo configurar Retrofit y OkHttp con Kotlinx Serialization para un endpoint DELETE que espera recibir Response<Unit> y validar códigos de respuesta HTTP?" | Gemini proveyó el contrato `suspend fun deletePost(@Path("id") id: Int): Response<Unit>` y el manejo en Repository comprobando `response.isSuccessful`. | Agregué captura de excepciones `CancellationException` para respetar la cancelación de corrutinas del ViewModel. | Para evitar que las excepciones de cancelación de corrutinas sean interceptadas incorrectamente como errores de red, cumpliendo las pautas obligatorias. |
| "Cuál es la forma idónea de estructurar los esquemas de color en Material 3 para un rediseño profesional sin tonos morados y con soporte dual claro/oscuro (#1565C0)?" | Gemini proporcionó una implementación de `lightColorScheme` y `darkColorScheme` basada en `Color(0xFF1565C0)` y tonos complementarios. | Personalicé los colores secundarios y de superficie (`BackgroundLight`, `SurfaceDark`) para mantener una consistencia académica y corporativa impecable. | Para cumplir estrictamente con la paleta de colores exigida en el PDF y evitar los colores predeterminados de plantillas básicas. |
| "Cómo estructurar el archivo PROMPTS.md para documentar las 4 mejoras de la Fase 2 cumpliendo con la tabla de 4 columnas requerida por el docente?" | Gemini generó una plantilla markdown estructurada con las columnas de prompt, respuesta de Gemini, cambios/descartes y justificación técnica. | Rellené la tabla con los casos de uso reales aplicados en el desarrollo de MuroCampus. | Para garantizar rigor académico y total transparencia en el uso de herramientas de IA durante la Fase 2 del laboratorio. |
