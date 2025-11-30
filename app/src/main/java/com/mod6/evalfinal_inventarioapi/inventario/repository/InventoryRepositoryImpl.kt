package com.mod6.evalfinal_inventarioapi.inventario.repository

import com.mod6.evalfinal_inventarioapi.InventoryApp
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoDao
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoEntity
import com.mod6.evalfinal_inventarioapi.inventario.data.toEntity
import com.mod6.evalfinal_inventarioapi.inventario.data.toRequest
import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementa lógica de negocio del contrato InventoryRepository.
 * Usa @Inject y @Singleton para que Hilt pueda inyectarla.
 */
@Singleton
class InventoryRepositoryImpl @Inject constructor(
    private val dao: ProductoDao,
    private val apiService: ProductoApiService
) : InventoryRepository { // <- Implementa la interfaz

    override val allProductos: Flow<List<ProductoEntity>> = dao.getAllProductos()

    // Sincroniza productos.
    override suspend fun syncProducts() {
        withContext(Dispatchers.IO) {
            try {
                val resp = apiService.getProductos(InventoryApp.USER_ID)
                if (resp.isSuccessful) {
                    val list = resp.body() ?: emptyList()
                    dao.deleteAll()
                    list.forEach { remote ->
                        dao.insert(remote.toEntity())
                    }
                } else {
                    // Log pendiente
                }
            } catch (e: IOException) {
                // Si no hay conectividad -> mantiene BD local
            } catch (e: HttpException) {
                // Error HTTP -> mantiene BD local
            }
        }
    }

    // Crea producto
    override suspend fun createProducto(nombre: String,
                                        descripcion: String,
                                        precio: Double,
                                        cantidad: Int) {
        withContext(Dispatchers.IO) {
            val maxId = dao.getMaxId() ?: 0
            val newId = maxId + 1
            val entity = ProductoEntity(
                id = newId,
                nombre = nombre,
                descripcion = descripcion,
                precio = precio,
                cantidad = cantidad,
                isSynced = false
            )
            // Insert local
            dao.insert(entity)

            // Intenta replicar nuevo registro al servidor con el mismo ID
            try {
                val response = apiService.createProducto(InventoryApp.USER_ID,
                    entity.toRequest())
                if (response.isSuccessful) {
                    // marca el reg. como sincronizado
                    dao.update(entity.copy(isSynced = true))
                } else {
                    // queda sin sync (isSynced = false)
                }
            } catch (e: Exception) {
                // deja como pendiente (isSynced = false)
            }
        }
    }

    // Actualiza producto.
    override suspend fun updateProducto(producto: ProductoEntity) {
        withContext(Dispatchers.IO) {
            val pending = producto.copy(isSynced = false)
            dao.update(pending)
            try {
                val resp = apiService.updateProducto(InventoryApp.USER_ID, producto.id,
                    pending.toRequest())
                if (resp.isSuccessful) {
                    dao.update(producto.copy(isSynced = true))
                }
            } catch (e: Exception) {
                // pendiente nuevas funcionalidades
            }
        }
    }

    // Elimina producto: primero reg. local, luego intenta eliminar remoto (API).
    override suspend fun deleteProducto(producto: ProductoEntity) {
        withContext(Dispatchers.IO) {
            dao.delete(producto)
            try {
                apiService.deleteProducto(InventoryApp.USER_ID, producto.id)
            } catch (e: Exception) {

            }
        }
    }
}