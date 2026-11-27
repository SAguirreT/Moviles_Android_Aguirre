package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import com.tecsup.murocampus.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostsViewModel(private val repository: PostRepository) : ViewModel() {

    private val _postsState = MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)
    val postsState: StateFlow<UiState<List<Post>>> = _postsState.asStateFlow()

    private val _detalleState = MutableStateFlow<UiState<Post>>(UiState.Cargando)
    val detalleState: StateFlow<UiState<Post>> = _detalleState.asStateFlow()

    private val _comentariosState = MutableStateFlow<UiState<List<Comentario>>>(UiState.Cargando)
    val comentariosState: StateFlow<UiState<List<Comentario>>> = _comentariosState.asStateFlow()

    private val _crearState = MutableStateFlow<UiState<Post>?>(null)
    val crearState: StateFlow<UiState<Post>?> = _crearState.asStateFlow()

    private val _eliminarState = MutableStateFlow<UiState<Unit>?>(null)
    val eliminarState: StateFlow<UiState<Unit>?> = _eliminarState.asStateFlow()

    private val _editarState = MutableStateFlow<UiState<Post>?>(null)
    val editarState: StateFlow<UiState<Post>?> = _editarState.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)
    val usuarioFiltro: StateFlow<Int?> = _usuarioFiltro.asStateFlow()

    private val _busquedaQuery = MutableStateFlow("")
    val busquedaQuery: StateFlow<String> = _busquedaQuery.asStateFlow()

    private var allPosts: List<Post> = emptyList()

    private val titulosEspañol = listOf(
        "Introducción a la Arquitectura MVVM en Android",
        "Gestión reactiva de estado con StateFlow y Corrutinas",
        "Consumo eficiente de servicios web REST con Retrofit y OkHttp",
        "Desarrollo de interfaces modernas con Jetpack Compose y Material 3",
        "Navegación avanzada y paso de argumentos en Compose",
        "Optimización del ciclo de vida en aplicaciones móviles",
        "Patrones de diseño orientados a componentes reutilizables",
        "Implementación de serialización con Kotlinx Serialization",
        "Manejo robusto de estados de carga, éxito y error en la capa de UI",
        "Buenas prácticas de desarrollo para aplicaciones móviles académicas"
    )

    private val cuerposEspañol = listOf(
        "La separación de incumbencias mediante MVVM permite mantener una base de código limpia, testeable y escalable en proyectos móviles modernos.",
        "StateFlow proporciona un flujo de datos observable que retiene el último valor y se adapta perfectamente al ciclo de vida con collectAsStateWithLifecycle.",
        "Retrofit simplifica la comunicación HTTP convirtiendo interfaces Kotlin en llamadas seguras y eficientes con soporte completo para corrutinas.",
        "Jetpack Compose permite construir interfaces declarativas fluidas aplicando Material 3 para lograr un diseño visual consistente y moderno.",
        "La navegación en Compose facilita la transición estructurada entre pantallas y el paso seguro de parámetros entre destinos.",
        "Comprender el ciclo de vida de la Activity y los ViewModels evita pérdidas de estado ante rotaciones de pantalla o cambios de configuración.",
        "La modularización y el uso de componentes reutilizables reducen la duplicación de código y mejoran la cohesión visual de la aplicación.",
        "Kotlinx Serialization ofrece un rendimiento óptimo en tiempo de compilación sin depender de reflexión en tiempo de ejecución.",
        "Gestionar explícitamente los estados de carga, éxito y error garantiza una experiencia de usuario fluida incluso ante fallos de red.",
        "Documentar el código y mantener una arquitectura estricta son pilares fundamentales en el desarrollo de software académico y profesional."
    )

    private val nombresComentarios = listOf("Carlos Mendoza", "Ana Torres", "Luis Quispe", "Maria Santos", "Jorge Ramos")
    private val cuerposComentarios = listOf(
        "Excelente explicación sobre los conceptos clave de la arquitectura.",
        "Me sirvió mucho para comprender el funcionamiento y la integración.",
        "El diseño de la interfaz quedó muy limpio, claro y profesional.",
        "Gracias por compartir este valioso aporte para la comunidad académica.",
        "Muy claro el ejemplo práctico presentado en esta publicación."
    )

    init {
        cargarPosts()
    }

    private fun traducirPost(post: Post): Post {
        val index = (post.id - 1).coerceAtLeast(0) % titulosEspañol.size
        return post.copy(
            title = titulosEspañol[index],
            body = cuerposEspañol[index]
        )
    }

    fun cargarPosts() {
        viewModelScope.launch {
            _postsState.value = UiState.Cargando
            val result = repository.getPosts()
            result.fold(
                onSuccess = { posts ->
                    allPosts = posts.map { traducirPost(it) }
                    aplicarFiltros()
                },
                onFailure = { error ->
                    _postsState.value = UiState.Error(error.localizedMessage ?: "Error desconocido al cargar publicaciones")
                }
            )
        }
    }

    fun filtrarPorUsuario(userId: Int?) {
        _usuarioFiltro.value = userId
        aplicarFiltros()
    }

    fun buscarPorTitulo(query: String) {
        _busquedaQuery.value = query
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val user = _usuarioFiltro.value
        val query = _busquedaQuery.value.trim().lowercase()

        var filtrados = allPosts
        if (user != null) {
            filtrados = filtrados.filter { it.userId == user }
        }
        if (query.isNotEmpty()) {
            filtrados = filtrados.filter { it.title.lowercase().contains(query) }
        }
        _postsState.value = UiState.Exito(filtrados)
    }

    fun seleccionarPost(postId: Int) {
        viewModelScope.launch {
            _detalleState.value = UiState.Cargando
            val result = repository.getPostById(postId)
            result.fold(
                onSuccess = { post -> _detalleState.value = UiState.Exito(traducirPost(post)) },
                onFailure = { err -> _detalleState.value = UiState.Error(err.localizedMessage ?: "Error al cargar detalle") }
            )
        }
        viewModelScope.launch {
            _comentariosState.value = UiState.Cargando
            val result = repository.getCommentsByPostId(postId)
            result.fold(
                onSuccess = { comentarios ->
                    val traducidos = comentarios.mapIndexed { idx, com ->
                        val cIndex = idx % nombresComentarios.size
                        com.copy(
                            name = nombresComentarios[cIndex],
                            body = cuerposComentarios[cIndex]
                        )
                    }.take(5)
                    _comentariosState.value = UiState.Exito(traducidos)
                },
                onFailure = { err -> _comentariosState.value = UiState.Error(err.localizedMessage ?: "Error al cargar comentarios") }
            )
        }
    }

    fun crearPost(titulo: String, contenido: String) {
        viewModelScope.launch {
            _crearState.value = UiState.Cargando
            val nuevo = NuevoPost(userId = 1, title = titulo, body = contenido)
            val result = repository.createPost(nuevo)
            result.fold(
                onSuccess = { created ->
                    _crearState.value = UiState.Exito(created)
                    allPosts = listOf(created) + allPosts
                    aplicarFiltros()
                },
                onFailure = { err ->
                    _crearState.value = UiState.Error(err.localizedMessage ?: "Error al crear publicacion")
                }
            )
        }
    }

    fun resetCrearState() {
        _crearState.value = null
    }

    fun eliminarPost(postId: Int) {
        viewModelScope.launch {
            _eliminarState.value = UiState.Cargando
            val result = repository.deletePost(postId)
            result.fold(
                onSuccess = {
                    _eliminarState.value = UiState.Exito(Unit)
                    allPosts = allPosts.filter { it.id != postId }
                    aplicarFiltros()
                },
                onFailure = { err ->
                    _eliminarState.value = UiState.Error(err.localizedMessage ?: "Error al eliminar")
                }
            )
        }
    }

    fun resetEliminarState() {
        _eliminarState.value = null
    }

    fun editarPost(postId: Int, nuevoTitulo: String) {
        viewModelScope.launch {
            _editarState.value = UiState.Cargando
            val result = repository.patchPost(postId, nuevoTitulo)
            result.fold(
                onSuccess = { updated ->
                    _editarState.value = UiState.Exito(updated)
                    allPosts = allPosts.map { if (it.id == postId) it.copy(title = updated.title) else it }
                    aplicarFiltros()
                },
                onFailure = { err ->
                    _editarState.value = UiState.Error(err.localizedMessage ?: "Error al editar")
                }
            )
        }
    }

    fun resetEditarState() {
        _editarState.value = null
    }
}
