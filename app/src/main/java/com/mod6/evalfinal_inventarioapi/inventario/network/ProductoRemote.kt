package com.mod6.evalfinal_inventarioapi.inventario.network

import com.google.gson.annotations.SerializedName

data class ProductoRemote(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("precio") val precio: Double,
    @SerializedName("cantidad") val cantidad: Int
)