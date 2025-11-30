package com.mod6.evalfinal_inventarioapi.di

import com.mod6.evalfinal_inventarioapi.inventario.network.ProductoApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // URL de la API.
    private const val BASE_URL = "http://api.example.com/"

    // Instancia Singleton de Retrofit.
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Instancia Singleton de ProductoApiService.
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ProductoApiService {
        return retrofit.create(ProductoApiService::class.java)
    }
}