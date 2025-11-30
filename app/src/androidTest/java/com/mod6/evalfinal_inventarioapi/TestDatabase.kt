package com.mod6.evalfinal_inventarioapi

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mod6.evalfinal_inventarioapi.inventario.data.AppDatabase

/**
 * Provee una instancia de base de datos Room para pruebas.
 * Utiliza ApplicationProvider para obtener un contexto de prueba simulado en la JVM.
 */
object TestDatabase {

    fun getTestInstance(): AppDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Utiliza un contexto simulado para construir la BBDD Room en memoria.
        return Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        )
            // permite que las pruebas runBlocking sincrónicas funcionen
            .allowMainThreadQueries()
            .build()
    }
}