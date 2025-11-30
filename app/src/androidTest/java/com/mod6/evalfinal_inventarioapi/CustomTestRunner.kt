package com.mod6.evalfinal_inventarioapi

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

/**
 * Runner de pruebas personalizado para habilitar la inyección de dependencias con Hilt en las pruebas.
 * Usa HiltTestApplication como la clase base de la aplicación de prueba.
 */
class CustomTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        // Usala clase HiltTestApplication para ejecutar el ambiente de pruebas.
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
