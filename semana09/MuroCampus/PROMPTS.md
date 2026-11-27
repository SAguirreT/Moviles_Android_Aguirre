# Registro de Prompts e Interacciones con Inteligencia Artificial (Gemini)

- **Autora:** Sheila Aguirre
- **Laboratorio:** 09 – Servicio Web
- **Proyecto:** MuroCampus
- **Rama:** `mejora-ia`

## Descripción de las mejoras implementadas
1. **Buscador de publicaciones por título:** Filtrado en tiempo real sobre la lista descargada con soporte para limpiar búsqueda y combinar con el filtro por usuario.
2. **Eliminación de publicaciones mediante DELETE:** Integración del endpoint DELETE con Retrofit `Response<Unit>`, diálogo de confirmación y manejo de simulación de JSONPlaceholder.
3. **Edición de publicaciones mediante PATCH:** Pantalla independiente para modificar el título mediante PATCH, mostrando el título original, validando campos no vacíos e informando la simulación del servidor.
4. **Presentación de Contenido en Español:** Mapeo y adaptación en la capa de presentación (ViewModel) de los títulos y cuerpos de posts y comentarios de JSONPlaceholder al español académico, preservando IDs, userIds y llamadas REST reales.
5. **Rediseño Visual Integral (Calidad Comercial / Google Play):** Transformación total de la interfaz con un sistema de diseño profesional inspirado en estándares de Figma y Dribbble. Incluye un encabezado superior exclusivo (`MuroCampusTopBar`), barra de navegación inferior personalizada (`MuroCampusBottomBar`), tarjetas de publicaciones con esquinas de 20 dp, avatares circulares, tipografía moderna jerarquizada (títulos de 26 sp, encabezados de 20 sp), paleta cromática refinada (`#196D9C`, `#E5F3FF`, `#F6FAFF`), botones redondeados de 24 dp y diálogos de eliminación avanzados con esquinas de 28 dp.

---

## Tabla de Prompts, Respuestas y Decisiones Técnicas

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| "Cómo implementar un buscador en tiempo real sobre una `LazyColumn` en Jetpack Compose manteniendo el filtrado por usuario activo sin llamadas repetitivas innecesarias a la API?" | Gemini sugirió utilizar `MutableStateFlow` combinando `allPosts`, `usuarioFiltro` y `busquedaQuery` dentro de una función de filtrado unificada en el ViewModel. | Ajusté la lógica para que ignore mayúsculas/minúsculas de manera robusta (`lowercase()`) y maneje correctamente estados vacíos. | Para asegurar rendimiento óptimo en memoria y evitar consultas innecesarias a la API por cada caracter ingresado en el buscador. |
| "Cómo configurar Retrofit y OkHttp con Kotlinx Serialization para un endpoint DELETE que espera recibir Response<Unit> y validar códigos de respuesta HTTP?" | Gemini proveyó el contrato `suspend fun deletePost(@Path("id") id: Int): Response<Unit>` y el manejo en Repository comprobando `response.isSuccessful`. | Agregué captura de excepciones `CancellationException` para respetar la cancelación de corrutinas del ViewModel. | Para evitar que las excepciones de cancelación de corrutinas sean interceptadas incorrectamente como errores de red, cumpliendo las pautas obligatorias. |
| "Cómo rediseñar integralmente MuroCampus para que tenga calidad de producto comercial (estándar Google Play) mediante un encabezado superior exclusivo, barra de navegación inferior, tarjetas de 20 dp y tipografías de 26 sp, manteniendo intacta la arquitectura MVVM?" | Gemini propuso centralizar los componentes personalizados en `Componentes.kt` (`MuroCampusTopBar`, `MuroCampusBottomBar`, `TarjetaPost`, etc.) aplicando esquinas de 20-28 dp, gradientes sutiles y sombras suaves. | Ajusté la composición general de `PantallaLista`, `PantallaDetalle`, `PantallaCrear` y `PantallaEditar` para integrar el nuevo sistema visual manteniendo la lógica y los modelos intactos. | Para cumplir con los requerimientos de un rediseño UI/UX senior, logrando una estética moderna, limpia, elegante y profesional. |
