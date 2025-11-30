package com.mod6.evalfinal_inventarioapi.inventario.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {
    @Query("SELECT * FROM productos ORDER BY id DESC")
    fun getAllProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos ORDER BY id DESC")
    fun getAllOnce(): List<ProductoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producto: ProductoEntity)

    @Update
    suspend fun update(producto: ProductoEntity)
    
    @Delete
    suspend fun delete(producto: ProductoEntity)

    @Query("DELETE FROM productos")
    suspend fun deleteAll()

    @Query("SELECT MAX(id) FROM productos")
    suspend fun getMaxId(): Int?
}