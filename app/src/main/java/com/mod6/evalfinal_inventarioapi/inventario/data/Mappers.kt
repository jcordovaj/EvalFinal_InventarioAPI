package com.mod6.evalfinal_inventarioapi.inventario.data

import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoRemote
import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoRequest

fun ProductoRemote.toEntity() = ProductoEntity(
    id          = id,
    nombre      = nombre,
    descripcion = descripcion,
    precio      = precio,
    cantidad    = cantidad,
    isSynced    = true
)

fun ProductoEntity.toRequest() = ProductoRequest(
    id          = id,
    nombre      = nombre,
    descripcion = descripcion,
    precio      = precio,
    cantidad    = cantidad
)