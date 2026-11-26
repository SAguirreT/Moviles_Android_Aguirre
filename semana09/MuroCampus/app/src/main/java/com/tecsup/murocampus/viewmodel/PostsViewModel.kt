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

    init {
        cargarPosts()
    }

    fun cargarPosts() {
        viewModelScope.launch {
            _postsState.value = UiState.Cargando
            val result = repository.getPosts()
            result.fold(
                onSuccess = { posts ->
                    allPosts = posts
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
                onSuccess = { post -> _detalleState.value = UiState.Exito(post) },
                onFailure = { err -> _detalleState.value = UiState.Error(err.localizedMessage ?: "Error al cargar detalle") }
            )
        }
        viewModelScope.launch {
            _comentariosState.value = UiState.Cargando
            val result = repository.getCommentsByPostId(postId)
            result.fold(
                onSuccess = { comentarios ->
                    val cincoComentarios = comentarios.take(5)
                    _comentariosState.value = UiState.Exito(cincoComentarios)
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
