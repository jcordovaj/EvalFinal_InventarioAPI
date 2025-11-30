package com.mod6.evalfinal_inventarioapi.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import javax.inject.Singleton

/**
 * Módulo Auxiliar: Inyecta el TestDispatcher en lugar del Dispatcher.Default o Dispatchers.IO.
 * Asegura que todo el trabajo asíncrono se resuelva en el TestDispatcher.
 */
@Module
@InstallIn(SingletonComponent::class)
object TestDispatchersModule {
    // Provee TestDispatcher para Inyecciones
    @Provides
    @Singleton
    fun provideTestDispatcher(): TestDispatcher = UnconfinedTestDispatcher()
}