package com.mod6.evalfinal_inventarioapi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class InventoryApp : Application() {
    companion object {
        val USER_ID = "test-user-123"
    }

    override fun onCreate() {
        super.onCreate()
    }
}