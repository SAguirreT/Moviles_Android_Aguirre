# MuroCampus - Laboratorio 09

- **Estudiante:** Sheila Aguirre
- **Usuario de GitHub:** SAguirreT
- **Proyecto:** MuroCampus
- **Carpeta:** `semana09/MuroCampus`
- **API utilizada:** https://jsonplaceholder.typicode.com/

## Descripción del proyecto
MuroCampus es una aplicación móvil desarrollada en Android utilizando Kotlin, Jetpack Compose y Material 3. Implementa una arquitectura MVVM estricta junto con el patrón Repository y Retrofit con Kotlinx Serialization para consumir los servicios web de JSONPlaceholder.

## Tecnologías utilizadas
- Kotlin 2.0.21
- Jetpack Compose & Material 3
- Retrofit 2.11.0 & OkHttp 4.12.0
- Kotlinx Serialization JSON 1.7.3
- Lifecycle Runtime Compose & ViewModel Compose 2.8.7
- Corutinas y StateFlow

## Árbol de paquetes
```text
com.tecsup.murocampus/
├── MainActivity.kt
├── model/
│   └── Modelos.kt
├── data/
│   ├── remote/
│   │   ├── PostApi.kt
│   │   └── RetrofitClient.kt
│   └── PostRepository.kt
├── viewmodel/
│   ├── UiState.kt
│   ├── PostsViewModel.kt
│   └── PostsViewModelFactory.kt
└── ui/
    ├── Componentes.kt
    ├── PantallaLista.kt
    ├── PantallaDetalle.kt
    ├── PantallaCrear.kt
    ├── PantallaEditar.kt
    └── theme/
```

## Diagrama de Arquitectura MVVM + Repository
```mermaid
graph TD
    UI[Pantallas Compose (UI)] -->|Observa StateFlow| VM[PostsViewModel]
    VM -->|Llama suspend fun| REPO[PostRepositoryRemote]
    REPO -->|HTTP GET / POST / DELETE / PATCH| API[PostApi (Retrofit)]
    API -->|JSONPlaceholder| WEB[(Servidor Web)]
```

## Capturas de pantalla obligatorias
1. **Lista de publicaciones:** Visualización de las 100 publicaciones en LazyColumn con tarjetas M3.
2. **Detalle y comentarios:** Visualización del post completo y 5 comentarios con correo en azul.
3. **Filtro por usuario:** Filtrado dinámico mediante `FilterChip` (Todos y Usuarios 1-10).
4. **Creación de publicación:** Formulario de alta con validación (`isNotBlank`) y POST.
5. **Error de conexión:** Pantalla de error con estado de reintento.

*(Las capturas se encuentran almacenadas en la carpeta `capturas/` del repositorio).*

## Preguntas de Para pensar (PDF)
1. **¿Por qué es importante que el Repository devuelva `Result` en lugar de lanzar excepciones directamente a la UI?**
   Permite encapsular el manejo de errores y excepciones de red de forma limpia en la capa de datos, facilitando que el ViewModel gestione estados de éxito y fallo (`UiState`) sin que la UI colapse por excepciones no controladas.
2. **¿Cuál es la ventaja de usar `kotlinx.serialization` frente a Gson en proyectos modernos con Kotlin?**
   Ofrece serialización en tiempo de compilación con soporte nativo de Kotlin, mayor rendimiento, seguridad de tipos estricta y menor uso de reflexión en tiempo de ejecución.
3. **¿Cómo garantiza `StateFlow` la persistencia del estado ante cambios de configuración como la rotación de pantalla?**
   `StateFlow` retiene el valor actual del estado en memoria y, al combinarse con `viewModelScope` y `collectAsStateWithLifecycle()`, sobrevive a los cambios de configuración del ciclo de vida de la Activity.
4. **¿Por qué no se debe realizar llamadas de red directamente desde los Composables?**
   Porque viola la separación de incumbencias de la arquitectura MVVM, bloquea el hilo principal si no se gestiona correctamente y provoca re-ejecuciones innecesarias e incontroladas durante la recomposición de la UI.
5. **¿Qué diferencia técnica existe entre `POST` y `PATCH` al interactuar con una API REST?**
   `POST` se utiliza para crear un recurso nuevo en el servidor, mientras que `PATCH` se emplea para actualizar parcialmente uno o más campos de un recurso existente.
6. **¿Por qué JSONPlaceholder no persiste permanentemente las modificaciones realizadas mediante POST, PUT, PATCH o DELETE?**
   Porque es un servidor API REST de pruebas y simulación (mock), por lo que simula las respuestas HTTP pero no almacena los cambios en una base de datos real.
