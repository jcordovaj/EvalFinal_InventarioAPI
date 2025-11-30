package com.mod6.evalfinal_inventarioapi.inventario.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.runner.RunWith
import com.mod6.evalfinal_inventarioapi.TestDatabase

// PRUEBA DE INTEGRACIÓN
// Verifica la funcionalidad de la interfaz ProductoDao.
@RunWith(AndroidJUnit4::class)
class ProductoDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: ProductoDao
    private val PRODUCTO_1 = ProductoEntity(1,
                                            "Laptop",
                                            "Potente",
                                            1200.0,
                                            5,
                                            true)
    private val PRODUCTO_2 = ProductoEntity(2,
                                            "Mouse",
                                            "Ergonómico",
                                            25.0,
                                            10,
                                            true)

    @Before
    fun createDb() {
        // Inicializa la BBDD en memoria usando para ejecutar la prueba.
        db  = TestDatabase.getTestInstance()
        dao = db.productoDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    // Prueba si el DAO puede insertar y luego mostrar los productos.
    @Test
    fun insertAndGetAllProductos_RetrievesCorrectly() = runBlocking {
        dao.insert(PRODUCTO_1)
        dao.insert(PRODUCTO_2)

        // Usa Flow.first() para obtener la lista asincrónica.
        val allProducts = dao.getAllProductos().first()

        assertEquals("Debe recuperar 2 productos", 2, allProducts.size)
        // El orden es DESC por ID, por lo que el ID 2 va primero.
        assertEquals("El primer producto debe ser el de ID 2",
            PRODUCTO_2.id,
            allProducts[0].id)
    }

    // Prueba si el DAO puede actualizar un producto existente.
    @Test
    fun updateProducto_UpdatesCorrectly() = runBlocking {
        dao.insert(PRODUCTO_1)

        val updatedName = "Laptop Pro Modificada"
        val updatedProduct = PRODUCTO_1.copy(nombre = updatedName, precio = 1500.0)
        dao.update(updatedProduct)

        val retrievedProduct = dao.getAllProductos().first().find { it.id == PRODUCTO_1.id }

        assertEquals("El nombre debe ser actualizado",
                     updatedName,
                        retrievedProduct?.nombre)
        assertEquals("El precio debe ser actualizado",
                     1500.0,
                        retrievedProduct?.precio)
    }

    // Prueba si el DAO puede eliminar un producto.
    @Test
    fun deleteProducto_DeletesCorrectly() = runBlocking {
        dao.insert(PRODUCTO_1)
        dao.insert(PRODUCTO_2)
        dao.delete(PRODUCTO_1)

        val allProducts = dao.getAllProductos().first()

        assertEquals("Debe quedar solo 1 producto",
                    1, allProducts.size)
        assertEquals("El producto restante debe ser PRODUCTO_2",
                    PRODUCTO_2.id,
                    allProducts[0].id)
    }
}