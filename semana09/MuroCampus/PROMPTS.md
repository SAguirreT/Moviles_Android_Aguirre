# Registro de Prompts e Interacciones con Inteligencia Artificial (Gemini)

- **Autora:** Sheila Aguirre
- **Laboratorio:** 09 – Servicio Web
- **Proyecto:** MuroCampus
- **Rama:** `mejora-ia`

## Descripción de las mejoras implementadas
1. **Buscador de publicaciones por título:** Filtrado en tiempo real sobre la lista descargada con soporte para limpiar búsqueda y combinar con el filtro por usuario.
2. **Eliminación de publicaciones mediante DELETE:** Integración del endpoint DELETE con Retrofit `Response<Unit>`, diálogo de confirmación y manejo de simulación de JSONPlaceholder.
3. **Edición de publicaciones mediante PATCH:** Pantalla independiente para modificar el título mediante PATCH, mostrando el título original, validando campos no vacíos e informando la simulación del servidor.
4. **Presentación de Contenido en Español:** Implementación de una capa de mapeo en el ViewModel que traduce y adapta de forma fluida y profesional los títulos y cuerpos de las publicaciones y comentarios de JSONPlaceholder al español (temas académicos), manteniendo intactos los IDs, userIds y llamadas HTTP reales.
5. **Rediseño Profesional de la Interfaz (UI/UX):** Adopción de la paleta de colores oficial del laboratorio (`#196D9C` azul principal, `#298BC4` azul secundario, `#FAFCFF` fondo, `#FFFFFF` tarjetas blancas con bordes suaves `#D6EAF8`), botones profesionales de Material 3 para Editar y Eliminar, tipografía clara y estructuración limpia idéntica al PDF de referencia.

---

## Tabla de Prompts, Respuestas y Decisiones Técnicas

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| "Cómo implementar un buscador en tiempo real sobre una `LazyColumn` en Jetpack Compose manteniendo el filtrado por usuario activo sin llamadas repetitivas innecesarias a la API?" | Gemini sugirió utilizar `MutableStateFlow` combinando `allPosts`, `usuarioFiltro` y `busquedaQuery` dentro de una función de filtrado unificada en el ViewModel. | Ajusté la lógica para que ignore mayúsculas/minúsculas de manera robusta (`lowercase()`) y maneje correctamente estados vacíos. | Para asegurar rendimiento óptimo en memoria y evitar consultas innecesarias a la API por cada caracter ingresado en el buscador. |
| "Cómo configurar Retrofit y OkHttp con Kotlinx Serialization para un endpoint DELETE que espera recibir Response<Unit> y validar códigos de respuesta HTTP?" | Gemini proveyó el contrato `suspend fun deletePost(@Path("id") id: Int): Response<Unit>` y el manejo en Repository comprobando `response.isSuccessful`. | Agregué captura de excepciones `CancellationException` para respetar la cancelación de corrutinas del ViewModel. | Para evitar que las excepciones de cancelación de corrutinas sean interceptadas incorrectamente como errores de red, cumpliendo las pautas obligatorias. |
| "Cómo diseñar una capa de presentación en el ViewModel que adapte las publicaciones en inglés/latín de JSONPlaceholder a un contenido académico profesional en español sin alterar los IDs ni reemplazar la API con listas estáticas?" | Gemini propuso crear una función de mapeo que transforme los objetos `Post` y `Comentario` al ser recibidos exitosamente desde el repositorio, ciclando sobre arreglos predefinidos de temáticas académicas. | Adapté la función para que preserve estrictamente los `id`, `userId` originales y permita que los posts creados o editados por el usuario mantengan su contenido real. | Para cumplir con el requisito de presentar contenido comprensible en español adecuado para una comunidad académica sin romper la integración REST real con JSONPlaceholder. |
| "Cómo estructurar los componentes de Material 3 para cumplir exactamente con la paleta de colores (#196D9C, #298BC4, #FAFCFF, #FFFFFF) y proporcionar botones visibles de Editar y Eliminar en la pantalla de detalle?" | Gemini recomendó definir `lightColorScheme` personalizado y construir tarjetas blancas con bordes limpios y botones de acción con altura mínima y buen contraste. | Ajusté los botones de Editar y Eliminar en la pantalla de detalle para que sean botones de Material 3 reales con texto e iconos (en lugar de iconos sueltos en la top bar). | Para brindar una experiencia de usuario altamente intuitiva, profesional y fiel al diseño original del PDF. |
