package com.mod6.evalfinal_inventarioapi.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mod6.evalfinal_inventarioapi.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ListaProductosUITest {

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @Test
    fun test_01_static_elements_are_displayed() {

        // Lanzamiento manual
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        // Bloqueo de foco/inicialización controlado.
        Thread.sleep(6000)

        // 1. Verifica elemento de UI: Título
        Espresso.onView(ViewMatchers.withText("Inventario Global de Productos"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // 2. Verifica elemento de UI: FAB
        // Si el FAB existe, el test pasa
        Espresso.onView(ViewMatchers.withId(R.id.fabAdd))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        scenario.close()
    }
}