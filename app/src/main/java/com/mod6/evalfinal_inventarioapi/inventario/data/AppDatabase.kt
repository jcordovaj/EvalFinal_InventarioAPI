package com.mod6.evalfinal_inventarioapi.inventario.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ProductoEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productoDao(): ProductoDao
}