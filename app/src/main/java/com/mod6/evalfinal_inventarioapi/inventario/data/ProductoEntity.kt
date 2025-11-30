package com.mod6.evalfinal_inventarioapi.inventario.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// Entidad de Producto para persistencia local con Room.
@Entity(tableName = "productos")
data class ProductoEntity(
    @PrimaryKey(autoGenerate = false)
    val id         : Int,
    val nombre     : String,
    val descripcion: String,
    val precio     : Double,
    val cantidad   : Int,
    val isSynced   : Boolean = true
)
