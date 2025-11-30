package com.mod6.evalfinal_inventarioapi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoEntity
import com.mod6.evalfinal_inventarioapi.inventario.repository.InventoryRepository
import com.mod6.evalfinal_inventarioapi.viewmodel.InventoryViewModel
import com.mod6.evalfinal_inventarioapi.viewmodel.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

// PRUEBA UNITARIA: Verifica la lógica de negocio del InventoryViewModel.
@OptIn(ExperimentalCoroutinesApi::class)
class InventoryViewModelTest {
    // Regla 1: Ejecución inmediata para testear LiveData/StateFlows.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // Regla 2: Clase auxiliar para testear funciones suspend y Coroutines en JUnit.
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // Mock del Repositorio.
    private val mockRepository: InventoryRepository = mockk()

    // System under testing (SUT).
    private lateinit var viewModel: InventoryViewModel

    // Flujo simulado para controlar la lista de productos que el ViewModel observa.
    private val productsFlow = MutableSharedFlow<List<ProductoEntity>>(replay = 1)

    @Before
    fun setup() = runTest(mainDispatcherRule.testDispatcher) {
        // 1. Configuración de Mocks:
        coEvery { mockRepository.allProductos } returns productsFlow
        coEvery { mockRepository.syncProducts() } returns Unit

        // 2. Inicialización del ViewModel.
        viewModel = InventoryViewModel(mockRepository)

        // 3. Estado inicial para el StateFlow de productos.
        productsFlow.emit(emptyList())
    }

    // PRUEBA 1: Verifica sincronización y manejo de errores
    @Test
    fun loadAndSyncProducts_onError_shouldEmitError() =
        runTest(mainDispatcherRule.testDispatcher) {
        // 1. Arrange: Simula que la sincronización falla con una excepción
        val errorMessage = "Fallo la API"
        // Configura mock para que lance excepción en la llamada a syncProducts()
        coEvery { mockRepository.syncProducts() } throws Exception(errorMessage)

        // 2. Act: Llama a la función de sincronización
        viewModel.loadAndSyncProducts()

        // 3. Assert: Verificar que el estado de UI es Error y contiene el mensaje
        val uiState = viewModel.uiState.value
        assertTrue(uiState is UiState.Error)
        assertEquals("Error sincronizando: $errorMessage",
            (uiState as UiState.Error).error)
    }

    // PRUEBA 2: Verificar la adición y validación con datos inválidos
    @Test
    fun addProduct_withInvalidData_shouldEmitValidationErrorAndNotCallRepository() =
        runTest(mainDispatcherRule.testDispatcher) {
        // 1. Arrange: Datos inválidos (precio <= 0.0)
        val precioInvalido = 0.0

        // 2. Act: Intenta inyectar datos que deben fallar a la validación interna
        viewModel.addProduct("Producto inválido",
            "Desc", precioInvalido, 1)

        // 3. Assert:
        // A) Verificar que el repositorio NUNCA fue llamado
        coVerify(exactly = 0) { mockRepository.createProducto(any(),
            any(), any(), any()) }

        // B) Verificar que el estado final de la UI es el error de validación
        val uiState = viewModel.uiState.value
        assertTrue(uiState is UiState.Error)
        val errorMessage = (uiState as UiState.Error).error
        assertTrue(errorMessage.contains("Validación: nombre, precio o cantidad inválidos"))
    }

    // PRUEBA 3: Verificar la adición con datos válidos
    @Test
    fun addProduct_withValidData_shouldCallRepositoryAndEmitSuccess() =
        runTest(mainDispatcherRule.testDispatcher) {
        // 1. Arrange: Datos válidos. Simulamos que la creación en el repo es exitosa.
        coEvery { mockRepository.createProducto(any(), any(), any(),
            any()) } returns Unit

        // 2. Act: Llama al ViewModel
        viewModel.addProduct("Laptop", "Para trabajo remoto",
            999.99, 5)

        // 3. Assert:
        // A) Verifica que el repositorio fue llamado una sola vez con los datos correctos
        coVerify(exactly = 1) { mockRepository.createProducto("Laptop",
            "Para trabajo remoto", 999.99, 5) }

        // B) Verifica el estado final de la UI
        assertEquals(UiState.Success("Producto agregado"),
            viewModel.uiState.value)
    }

    // PRUEBA 4: Eliminación de producto
    @Test
    fun deleteProduct_shouldCallRepositoryAndEmitSuccess() =
        runTest(mainDispatcherRule.testDispatcher) {
        // 1. Arrange: Setup.
        val productToDelete = ProductoEntity(
            id          = 1,
            nombre      = "Producto a Borrar",
            descripcion = "Temp",
            precio      = 1.0,
            cantidad    = 1,
            isSynced    = true
        )

        // Mock con delay(1) para forzar la suspensión
        coEvery { mockRepository.
            deleteProducto(productToDelete) } coAnswers { delay(1); Unit }

        // Recolectar estados de UI
        val results = mutableListOf<UiState>()
        val job = launch {
            viewModel.uiState.toList(results)
        }

        // Avanza el tiempo para capturar el estado Idle
        mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(1)

        // 2. Act: Llama a método de eliminación
        viewModel.deleteProduct(productToDelete)

        // 3. Delay que espera todas las corrutinas
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // 4. Assert:
        coVerify(exactly = 1) { mockRepository.deleteProducto(productToDelete) }
        assertTrue("La lista de resultados debería tener 3 estados (Idle, Loading," +
                " Success/Error). Actual: ${results.size}", results.size == 3)

        // Verifica la transición:
        assertTrue(results[0] is UiState.Idle)
        assertTrue(results[1] is UiState.Loading)

        // Verifica el estado final: Success
        assertTrue("El estado final esperado era Success, pero se obtuvo: ${results[2]}",
            results[2] is UiState.Success)

        // Verifica el mensaje de éxito
        assertEquals("Producto eliminado",
            (results[2] as UiState.Success).message)

        job.cancel()
    }
}