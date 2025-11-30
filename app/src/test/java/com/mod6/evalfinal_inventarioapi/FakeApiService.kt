package com.mod6.evalfinal_inventarioapi

import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoApiService
import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoRemote
import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoRequest
import retrofit2.Response

/**
 * Mock de ProductoApiService para aislar el repositorio y el ViewModel de las
 * llamadas reales a la red durante las pruebas unitarias.
 */
class FakeApiService : ProductoApiService {
    override suspend fun checkHealth(user: String) =
        Response.success<Map<String, String>>(mapOf(
            "status" to "ok",
            "user" to user
        ))

    // Retorna emptyList por defecto
    override suspend fun getProductos(user: String) =
        Response.success(emptyList<ProductoRemote>())

    // Simula la creación retornando el objeto con los datos enviados
    override suspend fun createProducto(user: String, product: ProductoRequest) =
        Response.success(ProductoRemote(
            id          = product.id ?: 999,
            nombre      = product.nombre,
            descripcion = product.descripcion,
            precio      = product.precio,
            cantidad    = product.cantidad
        ))

    // Simula la actualización
    override suspend fun updateProducto(user: String, producto_id: Int, product: ProductoRequest) =
        Response.success(ProductoRemote(producto_id,
            product.nombre,
            product.descripcion,
            product.precio,
            product.cantidad))

    // Simula el borrado
    override suspend fun deleteProducto(user: String, producto_id: Int) =
        Response.success(Unit)
}