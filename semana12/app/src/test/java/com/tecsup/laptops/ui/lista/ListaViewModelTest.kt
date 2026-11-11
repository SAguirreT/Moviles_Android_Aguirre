package com.tecsup.laptops.ui.lista

import com.tecsup.laptops.data.repository.ItemRepository
import com.tecsup.laptops.domain.model.Item
import com.tecsup.laptops.notification.Notificador
import com.tecsup.laptops.ui.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

// 1. FakeItemRepository y FakeNotificador escritos a mano (Sin Mockito)
class FakeItemRepository(
    initialItems: List<Item> = emptyList(),
    var refrescarResult: Result<Int> = Result.success(0),
    var cantidadGuardadaCustom: Int = initialItems.size
) : ItemRepository {

    val itemsFlow = MutableStateFlow(initialItems)
    override val items: Flow<List<Item>> = itemsFlow

    var ultimoIdAlternado: Int? = null

    override fun getFavoritos(): Flow<List<Item>> = MutableStateFlow(emptyList())

    override suspend fun refrescar(): Result<Int> {
        return refrescarResult
    }

    override fun cantidadGuardada(): Int {
        return if (itemsFlow.value.isNotEmpty()) itemsFlow.value.size else cantidadGuardadaCustom
    }

    override suspend fun alternarFavorito(id: Int) {
        ultimoIdAlternado = id
    }

    override suspend fun getLaptopById(id: Int): Item? {
        return itemsFlow.value.find { it.id == id }
    }

    override suspend fun limpiarCache() {
        itemsFlow.value = emptyList()
    }
}

class FakeNotificador : Notificador {
    val mensajes = mutableListOf<Pair<String, String>>()

    override fun mostrar(titulo: String, texto: String) {
        mensajes.add(Pair(titulo, texto))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `refresco exitoso actualiza estado a Exito y notifica`() = runTest {
        // Given
        val itemsPrueba = listOf(Item(1, "MacBook Pro", "M3 Max", 2499.00, "Apple", "url"))
        val fakeRepo = FakeItemRepository(
            initialItems = itemsPrueba,
            refrescarResult = Result.success(1)
        )
        val fakeNotificador = FakeNotificador()

        // When
        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Exito)
        assertEquals(itemsPrueba, (state as UiState.Exito).datos)
        assertEquals(1, fakeNotificador.mensajes.size)
        assertEquals("Se guardaron 1 elementos", fakeNotificador.mensajes[0].second)
    }

    @Test
    fun `fallo de red sin datos guardados muestra Error de conexion`() = runTest {
        // Given
        val fakeRepo = FakeItemRepository(
            initialItems = emptyList(),
            refrescarResult = Result.failure(IOException("Timeout")),
            cantidadGuardadaCustom = 0
        )
        val fakeNotificador = FakeNotificador()

        // When
        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("No hay conexión a internet", (state as UiState.Error).mensaje)
    }

    @Test
    fun `fallo de red con datos guardados mantiene Exito y emite aviso`() = runTest {
        // Given
        val itemsGuardados = listOf(Item(2, "Dell XPS", "OLED", 1999.00, "Dell", "url"))
        val fakeRepo = FakeItemRepository(
            initialItems = itemsGuardados,
            refrescarResult = Result.failure(IOException("Sin red")),
            cantidadGuardadaCustom = 1
        )
        val fakeNotificador = FakeNotificador()

        // When
        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)

        // Then
        val state = viewModel.uiState.value
        assertTrue(state is UiState.Exito)
        assertEquals(itemsGuardados, (state as UiState.Exito).datos)
        assertEquals("Sin conexión: mostrando datos guardados", viewModel.aviso.value)
    }

    @Test
    fun `alternarFavorito delega correctamente en el repositorio`() = runTest {
        // Given
        val fakeRepo = FakeItemRepository(initialItems = listOf(Item(5, "ThinkPad", "X1", 1200.00, "Lenovo", "url")))
        val fakeNotificador = FakeNotificador()
        val viewModel = ListaViewModel(fakeRepo, fakeNotificador)

        // When
        viewModel.alternarFavorito(5)

        // Then
        assertEquals(5, fakeRepo.ultimoIdAlternado)
    }
}
