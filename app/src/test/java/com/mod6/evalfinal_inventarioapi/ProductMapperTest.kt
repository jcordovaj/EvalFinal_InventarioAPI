package com.mod6.evalfinal_inventarioapi

import com.mod6.evalfinal_inventarioapi.inventario.data.toEntity
import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoRemote
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Clase para probar la correcta conversión de modelos de red a modelos de base de datos local.
 * Esta prueba valida la lógica de la función de extensión 'toEntity'.
 */
class ProductoMapperTest {
    @Test
    fun `remote to entity mapea correctamente`() {
        val remote = ProductoRemote(
            id = 1,
            nombre      = "Zapato",
            descripcion = "Descripción del zapato",
            precio      = 10.0,
            cantidad    = 2
        )
        // La función de extensión toEntity se importa y se usa como si fuera un
        // método de ProductoRemote
        val entity = remote.toEntity()

        assertEquals(remote.nombre, entity.nombre)
        assertEquals(remote.descripcion, entity.descripcion)

        // Compara Doubles/Floats
        assertEquals(remote.precio, entity.precio, 0.0)
        assertEquals(remote.cantidad, entity.cantidad)
    }
}