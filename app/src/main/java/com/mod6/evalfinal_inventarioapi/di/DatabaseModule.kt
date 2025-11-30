package com.mod6.evalfinal_inventarioapi.di

import android.content.Context
import androidx.room.Room
import com.mod6.evalfinal_inventarioapi.inventario.data.AppDatabase
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Instancia Singleton de la base de datos Room.
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "inventory_database" // Nombre de la BBDD
        ).build()
    }

    // Instancia Singleton de ProductoDao.
    @Provides
    @Singleton
    fun provideProductoDao(appDatabase: AppDatabase): ProductoDao {
        return appDatabase.productoDao()
    }
}