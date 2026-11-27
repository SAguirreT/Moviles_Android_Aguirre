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
5. **Rediseño Visual Premium (Nivel Comercial / Google Play):** Elevación completa de la interfaz de usuario con tarjetas blancas con esquinas de 18 dp, sombras suaves, bordes delicados (`#DCECF7`), paleta cromática refinada (`#196D9C` azul principal, `#2589C7` azul brillante, `#E8F5FF` celeste pastel, `#F8FBFF` fondo general), botones redondeados de 24 dp, tipografía moderna jerarquizada, avatares circulares y estados visuales elegantes (carga, error sin conexión y éxito).

---

## Tabla de Prompts, Respuestas y Decisiones Técnicas

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| "Cómo implementar un buscador en tiempo real sobre una `LazyColumn` en Jetpack Compose manteniendo el filtrado por usuario activo sin llamadas repetitivas innecesarias a la API?" | Gemini sugirió utilizar `MutableStateFlow` combinando `allPosts`, `usuarioFiltro` y `busquedaQuery` dentro de una función de filtrado unificada en el ViewModel. | Ajusté la lógica para que ignore mayúsculas/minúsculas de manera robusta (`lowercase()`) y maneje correctamente estados vacíos. | Para asegurar rendimiento óptimo en memoria y evitar consultas innecesarias a la API por cada caracter ingresado en el buscador. |
| "Cómo configurar Retrofit y OkHttp con Kotlinx Serialization para un endpoint DELETE que espera recibir Response<Unit> y validar códigos de respuesta HTTP?" | Gemini proveyó el contrato `suspend fun deletePost(@Path("id") id: Int): Response<Unit>` y el manejo en Repository comprobando `response.isSuccessful`. | Agregué captura de excepciones `CancellationException` para respetar la cancelación de corrutinas del ViewModel. | Para evitar que las excepciones de cancelación de corrutinas sean interceptadas incorrectamente como errores de red, cumpliendo las pautas obligatorias. |
| "Cómo estructurar un sistema de diseño comercial (nivel Google Play) en Material 3 aplicando esquinas de 18 dp, avatares circulares, tarjetas elevadas y botones de 24 dp sin alterar la lógica MVVM existente?" | Gemini proporcionó directrices para centralizar colores en `Color.kt`, tipografías en `Type.kt` y rediseñar los componentes (`TarjetaPost`, `PantallaLista`, `PantallaDetalle`, etc.) con contenedores limpios, divisores sutiles y acciones claras. | Apliqué radios de 18 dp en tarjetas, 24 dp en botones de acción y cápsulas de 20 dp en chips de usuario, garantizando un aspecto moderno y profesional. | Para cumplir con los más altos estándares de diseño UI/UX móvil, elevando MuroCampus a una categoría comercial y pulida. |
