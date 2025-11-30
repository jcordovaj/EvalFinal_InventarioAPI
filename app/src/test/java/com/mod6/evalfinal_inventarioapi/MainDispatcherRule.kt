package com.mod6.evalfinal_inventarioapi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Regla de JUnit que configura el Dispatcher principal (Main) para las corrutinas
 * en el contexto de pruebas.
 *
 * @param testDispatcher El dispatcher de prueba a usar. Por defecto, UnconfinedTestDispatcher.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        // Antes de cada prueba, reemplaza el Dispatchers.Main con el de prueba.
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        // Después de cada prueba, restablece el Dispatchers.Main a su valor original.
        Dispatchers.resetMain()
    }
}