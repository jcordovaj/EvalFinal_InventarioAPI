package com.mod6.evalfinal_inventarioapi.inventario.network

import retrofit2.Response
import retrofit2.http.*

interface ProductoApiService {
    @GET("{user}/health")
    suspend fun checkHealth(@Path("user") user: String): Response<Map<String, String>?>

    @GET("{user}/productos")
    suspend fun getProductos(@Path("user") user: String): Response<List<ProductoRemote>>

    @POST("{user}/productos")
    suspend fun createProducto(
        @Path("user") user: String,
        @Body product: ProductoRequest
    ): Response<ProductoRemote>

    @PUT("{user}/productos/{producto_id}")
    suspend fun updateProducto(
        @Path("user") user: String,
        @Path("producto_id") id: Int,
        @Body product: ProductoRequest
    ): Response<ProductoRemote>

    @DELETE("{user}/productos/{producto_id}")
    suspend fun deleteProducto(
        @Path("user") user: String,
        @Path("producto_id") id: Int
    ): Response<Unit>
}