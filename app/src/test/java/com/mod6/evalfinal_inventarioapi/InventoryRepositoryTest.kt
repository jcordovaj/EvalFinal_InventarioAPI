package com.mod6.evalfinal_inventarioapi

import com.mod6.evalfinal_inventarioapi.inventario.repository.InventoryRepository
import com.mod6.evalfinal_inventarioapi.inventario.repository.InventoryRepositoryImpl
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoDao
import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoApiService
import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoRequest
import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoRemote
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response

/**
 * PRUEBA UNITARIA: Verifica la lógica de sincronización del InventoryRepository.
 * Utiliza Mockk para interceptar el USER_ID de InventoryApp y asegurar que la prueba
 * utiliza un ID de usuario simulado.
 */
class InventoryRepositoryTest {
    private lateinit var mockDao       : ProductoDao
    private lateinit var mockApiService: ProductoApiService
    private lateinit var repository    : InventoryRepository
    private val TEST_USER_ID = "test-user-123"

    @Before
    fun setup() {
        // Inicializa Mocks
        mockDao        = mockk(relaxed = true)
        mockApiService = mockk(relaxed = true)

        // Mock del Companion Object de InventoryApp
        mockkObject(InventoryApp.Companion)
        every { InventoryApp.USER_ID } returns TEST_USER_ID

        // Inicializa el repositorio
        repository = InventoryRepositoryImpl(mockDao, mockApiService)

        clearAllMocks()
    }

    @After
    fun teardown() {
        // Deshace el Mockk
        unmockkAll()
    }

    // Prueba 1: Creación exitosa de producto.
    @Test
    fun createProducto_ApiSuccess_InsertsAndSyncs() = runBlocking {
        // Simula que el DAO devuelve el ID máximo de 10
        coEvery { mockDao.getMaxId() } returns 10

        val newProductId = 11
        val successRemote = ProductoRemote(
            id          = newProductId,
            nombre      = "Nuevo",
            descripcion = "test",
            precio      = 100.0,
            cantidad    = 5
        )
        // Simula respuesta exitosa de la API
        coEvery { mockApiService.createProducto(any(),
            any()) } returns Response.success(successRemote)

        repository.createProducto(
            nombre      = "Nuevo Producto",
            descripcion = "Desc",
            precio      = 100.0,
            cantidad    = 5
        )

        // Verifica la secuencia de operaciones: [1. Obtener ID], 2. Insertar, 3. llamar a API,
        // 4. Actualizar
        coVerify(ordering = Ordering.SEQUENCE) {
            // 1. Obtiene Id (getMaxId)
            mockDao.getMaxId()

            // 2. Insert local con isSynced=false y ID calculado (10 + 1 = 11)
            mockDao.insert(match { it.id == 11 && !it.isSynced })

            // 3. Llamada a la API (usando el TEST_USER_ID mockeado)
            mockApiService.createProducto(TEST_USER_ID, any<ProductoRequest>())

            // 4. Actualización local con isSynced=true
            mockDao.update(match { it.id == 11 && it.isSynced })
        }
    }

    // Prueba 2: Creación de producto que falla en la sincronización.
    @Test
    fun createProducto_ApiFailure_LeavesProductUnsynced() = runBlocking {
        // Simula que el DAO devuelve el ID máximo = 5
        coEvery { mockDao.getMaxId() } returns 5

        // Simula que la API responde con Error 500
        coEvery { mockApiService.createProducto(any(),
            any()) } returns Response.error(500, mockk(relaxed = true))

        repository.createProducto(
            nombre      = "Fallo Sync",
            descripcion = "Desc",
            precio      = 1.0,
            cantidad    = 1
        )

        // Verifica la secuencia de operaciones: [1. Obtener ID], 2. Insertar local y
        // 3. Llamada a la API
        coVerify(ordering = Ordering.SEQUENCE) {
            // 1. Obtiene Id (getMaxId)
            mockDao.getMaxId()
            // 2. Inserta local
            mockDao.insert(match { it.id == 6 && !it.isSynced })
            // 3. Llamada a la API (usando el TEST_USER_ID mockeado)
            mockApiService.createProducto(TEST_USER_ID, any<ProductoRequest>())
        }

        // Verifica que NO se llama a la actualización con isSynced=true
        coVerify(exactly = 0) { mockDao.update(match { it.isSynced }) }
    }
}