package com.mod6.evalfinal_inventarioapi.di

import com.mod6.evalfinal_inventarioapi.inventario.repository.InventoryRepository
import com.mod6.evalfinal_inventarioapi.inventario.repository.InventoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindInventoryRepository(
        inventoryRepositoryImpl: InventoryRepositoryImpl
    ): InventoryRepository
}