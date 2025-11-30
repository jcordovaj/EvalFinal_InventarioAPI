package com.mod6.evalfinal_inventarioapi.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.mod6.evalfinal_inventarioapi.R

class SplashActivity : AppCompatActivity() {

    private val splashDelay: Long = 2000 // 2 segundos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Handler para administrar una espera y luego lanzar la actividad principal
        Handler(Looper.getMainLooper()).postDelayed({
            // 1. Crea un Intent para ir a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            // 2. Inicia MainActivity
            startActivity(intent)
            // 3. Finaliza la actividad para que el usuario no pueda volver con el botón "Atrás"
            finish()
        }, splashDelay)
    }
}