package com.mod6.evalfinal_inventarioapi.inventario.repository

import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoEntity
import kotlinx.coroutines.flow.Flow

// Define las operaciones públicas del Repositorio.
interface InventoryRepository {
    // Propiedad flow: la implementación la manejará.
    val allProductos: Flow<List<ProductoEntity>>

    suspend fun syncProducts()

    suspend fun createProducto(nombre: String, descripcion: String, precio: Double, cantidad: Int)

    suspend fun updateProducto(producto: ProductoEntity)

    suspend fun deleteProducto(producto: ProductoEntity)
}